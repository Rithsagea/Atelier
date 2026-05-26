# CORE — Core Abstractions

_Load when: touching anything in `packages/core`, or any content that contributes to stats,
serializes, or references other composites._

These live in `packages/core`. Understand these before touching anything else.

---

## Composite as universal substrate

**Composite is the universal building block. Everything is a composite by default.**
Capabilities — identity, serialization, structure, contribution to stats — are aspects
attached to a composite, not intrinsic properties of bespoke classes.

The burden of proof is inverted: a thing is a composite unless there is a _specific_
reason it should not be. "This doesn't need a composite" is not a reason — a non-composite
living in a composite-everything system is the anomaly that breaks uniformity, and that
cost is usually larger than the local cost of the composite. Reserve non-composite typed
values for leaves with no independent capability surface.

This is a deliberate adoption of an ECS-style pattern (see the reversal note in `PLAN.md`).
The value being bought is _uniformity_: one substrate, one discovery primitive, one extension
mechanism, applied fractally at every level (a scope of entities, a composite of aspects).

---

## Aspect\<T>

A typed capability slot. The primitive everything builds on.

```ts
const HpAspect = new Aspect<HitPoints>("HitPoints");
```

An `Aspect` has a stable symbol id and a name for error messages. The name additionally
serves as the **stable serialization key** — aspect names must be globally unique, with
conflicts detected once at initialization.

---

## Composite

A container of aspects. No lifecycle state — it either has an aspect or it doesn't.

```ts
composite.provide(HpAspect, new HitPoints(30, 30));
composite.get(HpAspect); // throws with clear message if missing — this is intentional
composite.suppose(HpAspect); // returns T | undefined
```

**There is no Phase or lifecycle guard.** Missing aspect = content bug = thrown error.
The engine runs or it doesn't. Editing and running are mutually exclusive at the
session level, not the composite level. Serialization of incomplete composites works fine —
serialize whatever aspects exist, reconstruct on load.

A composite is intentionally minimal information that rigidifies as it is used. A transient
composite floating inside another may carry almost nothing; the moment it is referenced or
placed somewhere persistent it acquires the aspects (identity, serialization, structure)
that role demands. Structure accretes; it is not mandated up front.

---

## Capability discovery

The reusable discovery primitive is **filter a collection by capability**. It runs over a
composite's aspect values (intra-entity: a sheet's stat contributors) and over a scope's
composites (inter-entity: "find all sheets in the scene with a resist effect") with the
same shape. This is the single pattern reused across mechanics — feats that modify anything
on a sheet, environmental effects that query all sheets, etc.

Discovery is **dynamic and rebuilt on demand**, not maintained as a synced list. Because all
SRD content is treated as homebrew (homebrew is an extension of the base engine, not a
special case), the engine cannot enumerate participant _kinds_ in advance — so participation
is found by querying for a capability, never by a hardcoded source list or a cache that must
be kept in sync.

Capability is an **explicit, declared property** (a value opts in to the capability
interface), never discovered by reflecting over a value's shape or scanning a prototype for
decorated handlers. "Is this a contributor?" must be a checkable, type-visible fact. This is
why stat contribution does not use the event system (see Event System below).

---

## Scope

A closed graph of composites. Session is the top-level scope. Prefabs are sub-scopes
stamped into a session at instantiation.

**Session = Campaign.** A session does not map to a single play session — it is the
campaign itself, persistent indefinitely. There is no "session end." Anything that
needs to be transient is marked as such in the content file via unmarked (non-`@Property`)
fields. Anything serialized is explicitly decorated. The content author owns this
distinction entirely.

```ts
scope.get(compositeId); // throws if missing
scope.suppose(compositeId); // returns Composite | undefined
scope.all();
scope.allWhere(predicate);
```

`scope.allWhere` is the scope-level form of capability discovery — querying entities by the
aspects/capabilities they hold.

---

## CompositeRef

Typed wrapper for inter-composite references. **Never use raw strings for inter-composite refs.**

```ts
class Links {
  @Property.Map(CompositeRefContext) refs: Record<string, CompositeRef>;
}
```

At prefab definition time, IDs are local strings (`'leader'`, `'follower_1'`).
At stamp time, a resolution pass rewrites all `CompositeRef.id` fields from local → absolute UUIDs.
This is what makes prefabs reusable without ID collisions.

---

## Template / StructureAspect

Declares the aspects an entity type is expected to have. Used for validation and "what is
this?" identification.

The long-term direction is a **StructureAspect**: structure becomes an aspect carried by the
composite (kind = item / trait / monster / prop / sheet, plus the expected aspect set) rather
than an external object the composite is checked against. Where the StructureAspect is
expected to be present it is _assumed_ present — its absence is a type error, consistent with
the "missing aspect = content bug = throw" stance.

StructureAspect is **derived, not declared twice**: the serializer generates the necessary
structural information from the `@Property` declarations rather than the author maintaining a
separate schema. (Not built yet — realized when complexity demands it.)

---

## Prefab

A definition of a group of related composites with local IDs. The `BandOfCultists` example:

```ts
const BandOfCultists = definePrefab({
  actors: {
    leader: { template: 'Monster', data: {...}, links: { followers: ['f1','f2','f3'] } },
    f1: { template: 'Monster', data: {...} },
    f2: { template: 'Monster', data: {...} },
    f3: { template: 'Monster', data: {...} },
  }
})

// stamping: generates UUIDs, rewrites ActorRefs, inserts into session scope
Scope.fromPrefab(BandOfCultists)
```

---

## Event System

Local events use `createEvent`, `@Subscribe`, `Emitter` — unchanged from original impl.
Network events extend this for the WS boundary:

```ts
const HpChanged = createNetworkEvent<{ compositeId: string; current: number }>("hp_changed", "s2c");
```

`NetworkEvent` carries a stable string id for wire format and direction (`c2s` | `s2c` | `both`).
Payload serialization uses the existing `serialize`/`deserialize` functions.

**Events are not used for stat contribution / capability discovery.** Discovering
contributors by broadcasting an event and letting aspect values subscribe was rejected (see
Rejections below). Events remain the right tool for genuine push/broadcast (HP changed, scene
advanced, chat).

---

## Stat Contribution

Computed stats (ability scores, and later AC, saves, skills) are a **pure projection** over
a set of contributors. Anything that writes to a stat exposes the contributor capability and
is discovered by capability query over the owning composite's values; the computed stat
aspect rebuilds itself by gathering and folding those contributions.

Key commitments (these are the content-facing contract — expensive to change because every
piece of content is written against them):

- **A contribution carries a thunk, not a precomputed number.** It computes its value from a
  stat-reader supplied at evaluation time. This is what allows conditional contributions
  (e.g. "+2 CON if final STR ≥ 13") and a future transition to fixed-point / topological
  ordering _without rewriting any content_ — the contributor's signature is identical whether
  it reads nothing or reads final stats.
- **Contributors receive the owning composite at evaluation time**, never closing over stats
  at construction. This is the sanctioned "reach back up" mechanism; sub-composite nesting is
  not required for it.
- **Contributions are returned as a list of structured objects, not bare lambdas**, so an
  ordering model can grow as a field on the contribution object later. Commutative
  contributors will not change when it does.

**Ordering is deliberately deferred.** Current scope is purely additive, and additive
contributions commute, so no ordering machinery exists (no stages, no topological sort, no
before/after edges). The first genuinely non-commutative operation (a cap, a "set to N"
debuff, a post-cap bonus) is the signal to add ordering — and it is added on the contribution
object, not on the discovery interface. The intended end state is explicit before/after
dependencies resolved by topological sort: non-unique order yields a warning, no valid order
errors. Build it when a real case forces it, not for hypotheticals.

---

## Serialization

`@Property` decorator namespace. Only decorated fields are serialized — unmarked fields are transient.

```ts
namespace Property {
  Primitive        // string, number, boolean
  Class<T>         // nested object with known constructor
  Multi(context)   // polymorphic — embeds $type tag using BiMap key
  List(context?)   // array
  Map(context?)    // record
}
```

`TypeMap` + `@Register` handle polymorphic class registries (Classes, Races, Spells, etc).

Serialization is **dynamic and locally consistent**: each polymorphic property carries the
typemap information needed to discover and reconstruct its values. Global ids are resolved
once at initialization, and conflicts are detected then. The whole graph is never centrally
schema'd — global consistency emerges from every local edge (every property's typemap) being
individually consistent.

### Transient-by-default and the purity law

Aspects are **transient by default**. An aspect persists only the fields it explicitly
decorates with `@Property`; everything else is rebuilt on load.

**The purity law:** _if it serializes, the serializer can reconstruct it; if it is transient,
it must be a pure function of the persisted aspects._ A transient aspect (e.g. computed
ability scores) holds no persisted state of its own and is regenerated on load by recomputing
from the persisted contributors.

The one studied way purity breaks is when a transient needs to remember _what was_ or _what
happened_ (a previous overwritten value, an ordering, a random roll) rather than _what is_.
The resolution is to **promote that aspect to persisted** — move the transient/persistent
boundary rather than violate the law. This keeps the change local (one aspect crosses the
line) instead of a rewrite. Randomness is already handled this way: rolls are logged scenario
events whose results are persisted as fixed values, so they enter folds as constants, never as
re-rolled functions.

Post-deserialization, an init pass fires on each reconstructed object so derived/transient
fields can be recomputed from the persisted ones.

---

## Rejections that justify core design

Kept here (rather than in `PLAN.md`) because "did we already reject this, and why" comes up
mid-implementation right where these abstractions live.

- **Event-based stat contribution / contributor discovery** — rejected. Discovering
  contributors by broadcasting an event and letting aspect values subscribe makes contributor
  identity ambiguous (an aspect slot's type can't tell you its value is also a listener;
  discovery becomes prototype reflection) and orders contributions by invisible
  handler-registration order, which is fatal once operations are non-commutative. Contribution
  is a declared capability discovered by query instead.

- **Maintained contributor list** — rejected as a redundant intermediary. A synced list of
  "who contributes" sitting between the components and recompute is a cache that must be kept
  in sync on every equip/unequip/feature-gain. Since the projection is cheap and pure,
  recompute rebuilds by querying the composite's values each refresh instead — no cache, no
  sync burden.
