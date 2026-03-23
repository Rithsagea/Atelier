# @atelier/core

Shared primitives for the Atelier D&D engine. No Bun APIs, no Svelte — safe to import from anywhere.

---

## Abstractions

### Aspect

A typed capability slot. The primitive everything builds on.

```ts
import { Aspect } from '@atelier/core/actor/Aspect'

const HpAspect = new Aspect<HitPoints>('HitPoints')
```

An `Aspect` has a stable symbol id (for map lookup) and a name (for error messages).

---

### Actor

A container of aspects. No lifecycle — it either has an aspect or it doesn't.

```ts
import { Actor } from '@atelier/core/actor/Actor'

actor.provide(HpAspect, new HitPoints(30, 30))
actor.get(HpAspect) // throws with clear message if missing — intentional
actor.suppose(HpAspect) // returns T | undefined
actor.has(HpAspect) // boolean
```

Missing aspect = content bug. The engine runs or it doesn't.

---

### ActorRef

Typed wrapper for inter-actor references. Never use raw strings.

```ts
import { ActorRef, ActorRefContext } from '@atelier/core/actor/ActorRef'

class Links {
  @Property.Map(ActorRefContext) refs: Record<string, ActorRef> = {}
}
```

At prefab definition time, ids are local strings (`'leader'`, `'f1'`). At stamp time,
`Scope.fromPrefab` rewrites all ids to absolute UUIDs.

---

### ActorTemplate

Declares required aspects for an entity type. Used for validation, not runtime enforcement.

```ts
import { ActorTemplate } from '@atelier/core/actor/ActorTemplate'

const PlayerCharacter = new ActorTemplate('PlayerCharacter', [
  AbilityScoresAspect,
  HpAspect,
  ClassAspect,
])

template.validate(actor) // throws listing all missing aspects
template.create() // returns empty Actor, does not validate
```

---

### Scope

A closed graph of actors. A session (`Campaign`) is the top-level scope.

```ts
import { Scope } from '@atelier/core/scope/Scope'

scope.get(actorId) // throws if missing
scope.suppose(actorId) // returns Actor | undefined
scope.all() // Actor[]
scope.allWhere(predicate) // filtered Actor[]
```

---

### Prefab

A reusable definition of a group of related actors with local IDs. At stamp time,
`Scope.fromPrefab` generates UUIDs and resolves all link references.

```ts
import { definePrefab } from '@atelier/core/scope/Prefab'
import { Scope } from '@atelier/core/scope/Scope'

const BandOfCultists = definePrefab({
  actors: {
    leader: { template: 'Monster', links: { followers: ['f1', 'f2', 'f3'] } },
    f1: { template: 'Monster' },
    f2: { template: 'Monster' },
    f3: { template: 'Monster' },
  },
})

const { scope, links, byLocalId } = Scope.fromPrefab(BandOfCultists)
// links['<leader-uuid>'].followers → ActorRef[] with resolved UUIDs
```

---

### Event System

```ts
import { createEvent, Subscribe, Emitter } from '@atelier/core/event/Event'

const DamageEvent = createEvent<{ amount: number }>('DamageEvent')

class Monster {
  @Subscribe(DamageEvent)
  onDamage(data: { amount: number }) { ... }
}

const emitter = new Emitter()
emitter.addListener(monster)
emitter.call(DamageEvent, { amount: 5 })
emitter.removeListener(monster)
```

Handler discovery is metadata-driven — `addListener` scans the object's prototype
chain for `@Subscribe`-decorated methods. Handlers are sorted by priority (high first).

---

### Serialization

Only `@Property`-decorated fields are serialized. Unmarked fields are transient.

```ts
import { Property, TypeMap, Register, serialize, deserialize } from '@atelier/core/serial/Data'

// Primitives
@Property.Primitive name: string = ''

// Nested class
@Property.Class(HitPoints) hp?: HitPoints

// Polymorphic (embeds $type tag)
const Spells = new TypeMap<Spell>()

@Register(Spells)
class Fireball extends Spell { ... }

@Property.Multi(Spells) spell?: Spell

// Arrays and records
@Property.List() tags: string[] = []
@Property.List(new ClassSerializer(HitPoints)) hpList: HitPoints[] = []
@Property.Map(ActorRefContext) refs: Record<string, ActorRef> = {}
```

Post-deserialization, `InitializeObjectEvent` fires on the reconstructed object so
`@Subscribe(InitializeObjectEvent)` handlers can recompute derived fields.

```ts
import { InitializeObjectEvent } from '@atelier/core/serial/Data'

@Subscribe(InitializeObjectEvent)
init() {
  this.modifier = Math.floor((this.score - 10) / 2)
}
```

---

### Stats

D&D 5e ability and skill constants.

```ts
import { Abilities, Skills, AbilitySkills } from '@atelier/core/stats/Stats'
import type { Ability, Skill } from '@atelier/core/stats/Stats'
```

`Abilities`: `['strength', 'dexterity', 'constitution', 'intelligence', 'wisdom', 'charisma']`

`Skills`: all 18 skill names.

`AbilitySkills`: mapping of ability → its associated skills.

---

### Utilities

```ts
import { BiMap, topologicalSort } from '@atelier/core/util/Algorithms'
import { enumMap, isPrimitive } from '@atelier/core/util/Types'
import type { Constructor, Primitive } from '@atelier/core/util/Types'
```

`BiMap<K, V>` — bidirectional map with O(1) inverse lookup. `BiMap.from(record)` for
construction from a plain object.

`topologicalSort({ root, getChildren })` — DFS-based sort with cycle detection.

`enumMap(keys, fn)` — builds a `Record<K, V>` by mapping over a const key array.
