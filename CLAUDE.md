# Atelier — Project Context

A self-hosted D&D 5e tabletop app. Single Bun server, SvelteKit frontend, SQLite database.
Designed for personal use — ergonomics and clean abstractions are not to be compromised
even though there's only one user.

---

## Stack

- **Runtime:** Bun
- **Backend:** Hono (with Bun adapter)
- **Frontend:** SvelteKit + Tailwind
- **Database:** `bun:sqlite`
- **Language:** TypeScript strict mode, decorators enabled
- **Monorepo:** Bun workspaces — `packages/core`, `packages/server`, `packages/client`

---

## Monorepo Structure

```
packages/
├── core/        # shared types, primitives, base classes — no Bun APIs, no Svelte
├── server/      # Hono server, game engine, SRD content, CLI
└── client/      # SvelteKit frontend

content/         # user-defined homebrew (explicit index.ts entry point)
CLAUDE.md        # this file
```

---

## Monorepo Config

### Package aliases

All imports use package aliases. **Never use relative paths that cross package boundaries.**

```ts
import { Aspect } from "@atelier/core/actor/Aspect";
import { Subscribe } from "@atelier/core/event/Event";
import { AbilityScoresAspect } from "@atelier/server/aspects/AbilityScores";
```

`@atelier/core/*` for core primitives. `@atelier/server/*` for server internals.
Content files use both — never `../../packages/`.

### Root tsconfig.json

```json
{
  "compilerOptions": {
    "strict": true,
    "experimentalDecorators": true,
    "emitDecoratorMetadata": true,
    "moduleResolution": "bundler",
    "target": "ESNext",
    "module": "ESNext"
  }
}
```

### packages/core/tsconfig.json

```json
{
  "extends": "../../tsconfig.json",
  "compilerOptions": {
    "rootDir": "src",
    "paths": {
      "@atelier/core/*": ["./src/*"]
    }
  },
  "include": ["src"]
}
```

### packages/server/tsconfig.json

```json
{
  "extends": "../../tsconfig.json",
  "compilerOptions": {
    "paths": {
      "@atelier/core/*": ["../core/src/*"],
      "@atelier/server/*": ["./src/*"]
    }
  },
  "include": ["src", "../../content"]
}
```

Note `content` is included here — this gives content files type-checking and alias
resolution without any extra config on the content author's side.

### packages/client/tsconfig.json

```json
{
  "extends": "../../tsconfig.json",
  "compilerOptions": {
    "paths": {
      "$lib/*": ["./src/lib/*"],
      "@atelier/core/*": ["../core/src/*"]
    }
  }
}
```

### content/tsconfig.json

```json
{
  "extends": "../packages/server/tsconfig.json"
}
```

Extends server tsconfig so content files get `@atelier/core` and `@atelier/server`
aliases automatically. Content authors never touch tsconfig.

### Root package.json

```json
{
  "name": "atelier",
  "workspaces": ["packages/*"]
}
```

### packages/core/package.json

```json
{
  "name": "@atelier/core",
  "module": "src/index.ts"
}
```

### packages/server/package.json

```json
{
  "name": "@atelier/server",
  "dependencies": {
    "@atelier/core": "workspace:*"
  }
}
```

`workspace:*` resolves to the local package directly — no symlinks, no `bun link`.

### LSP

The setup works identically in AstroNvim, VSCode, and any editor using `tsserver`
and `svelte-language-server`. Both servers read `tsconfig.json` paths natively.

VSCode note: if you open the monorepo root folder, VSCode occasionally picks up the
wrong `tsconfig.json` for a file. Fix with a `.code-workspace` file if this occurs —
it's a VSCode UI issue, not a TypeScript config issue.

---

## Core Abstractions

These live in `packages/core`. Understand these before touching anything else.

### Aspect\<T>

A typed capability slot. The primitive everything builds on.

```ts
const HpAspect = new Aspect<HitPoints>("HitPoints");
```

An `Aspect` has a stable symbol id and a name for error messages. Nothing else.

### Actor

A container of aspects. No lifecycle state — it either has an aspect or it doesn't.

```ts
actor.provide(HpAspect, new HitPoints(30, 30));
actor.get(HpAspect); // throws with clear message if missing — this is intentional
actor.suppose(HpAspect); // returns T | undefined
```

**There is no Phase or lifecycle guard.** Missing aspect = content bug = thrown error.
The engine runs or it doesn't. Editing and running are mutually exclusive at the
session level, not the actor level. Serialization of incomplete actors works fine —
serialize whatever aspects exist, reconstruct on load.

### Scope

A closed graph of actors. Session is the top-level scope. Prefabs are sub-scopes
stamped into a session at instantiation.

**Session = Campaign.** A session does not map to a single play session — it is the
campaign itself, persistent indefinitely. There is no "session end." Anything that
needs to be transient is marked as such in the content file via unmarked (non-@Property)
fields. Anything serialized is explicitly decorated. The content author owns this
distinction entirely.

```ts
scope.get(actorId); // throws if missing
scope.suppose(actorId); // returns Actor | undefined
scope.all();
scope.allWhere(predicate);
```

### ActorRef

Typed wrapper for inter-actor references. **Never use raw strings for inter-actor refs.**

```ts
class Links {
  @Property.Map(ActorRefContext) refs: Record<string, ActorRef>;
}
```

At prefab definition time, IDs are local strings (`'leader'`, `'follower_1'`).
At stamp time, a resolution pass rewrites all `ActorRef.id` fields from local → absolute UUIDs.
This is what makes prefabs reusable without ID collisions.

### ActorTemplate

Declares required aspects for an entity type. Used for validation and documentation,
not runtime enforcement.

```ts
const PlayerCharacter = new ActorTemplate("PlayerCharacter", [
  AbilityScoresAspect,
  HpAspect,
  ClassAspect,
  RaceAspect,
  SkillsAspect,
]);

template.validate(actor); // throws listing all missing aspects
template.create(); // creates empty Actor, does not validate
```

### Prefab

A definition of a group of related actors with local IDs. The `BandOfCultists` example:

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

### Event System

Local events use `createEvent`, `@Subscribe`, `Emitter` — unchanged from original impl.
Network events extend this for the WS boundary:

```ts
const HpChanged = createNetworkEvent<{ actorId: string; current: number }>("hp_changed", "s2c");
```

`NetworkEvent` carries a stable string id for wire format and direction (`c2s` | `s2c` | `both`).
Payload serialization uses the existing `serialize`/`deserialize` functions.

### Serialization

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

---

## Content System

### Loading

Content is loaded at startup via explicit entry point — no magic glob scanning.

```ts
// server/src/content/index.ts — user writes this
import "./classes/Fighter";
import "./classes/Wizard";
import "./spells/Fireball";
// etc
```

`await import('./content/index.ts')` at server startup runs all decorator side effects,
populating TypeMaps. Content files don't need to export anything meaningful.

### Defining Content

```ts
// content/classes/Fighter.ts
@Register(Classes)
export class Fighter extends Class {
  readonly name = "Fighter";
  readonly hitDie = 10;

  @Subscribe(LoadEffectsEvent)
  loadEffects(actor: Actor) {
    actor.get(SavingThrowsAspect).addProficiency("strength");
    actor.get(SavingThrowsAspect).addProficiency("constitution");
  }
}
```

The `@Register` decorator is what matters — file location is irrelevant.
Users can structure their content directory however they like.

### Spells / Actions

Spells declare a `cast` method with a typed inputs argument. No Input schema system at P0 —
that's a frontend quality-of-life feature for generic renderers, not core infrastructure.

```ts
@Register(Spells)
class Fireball extends Spell {
  cast(actor: ReadyActor, inputs: { targetPoint: Point; slotLevel: number }) {
    actor.get(SpellSlotsAspect).consume(inputs.slotLevel);
    // apply damage to actors in range
  }
}
```

---

## Server Architecture

### Startup Sequence

```
1. import('./content/index.ts')     — populate TypeMaps, register NetworkEvents
2. runMigrations()                  — idempotent SQLite schema setup
3. registerHandlers(...)            — scan @ServerEvent metadata, wire WsRouter
4. Hono app.listen(port)            — REST + WS + static client files
5. cli.listen()                     — concurrent on stdin
```

### WS Architecture

`@ServerEvent` decorator registers handler methods on the `WsRouter` singleton.
`WsRouter.dispatch` deserializes payload and calls the handler.
`SessionRegistry` is in-memory (transient), holds live WS connections, handles broadcast.

**Critical split — HTTP vs WS:**

- HTTP: request/response shaped operations (load sheet, create actor, cast spell action)
- WS: push/broadcast only (HP changed, scene advanced, chat, dice results broadcast)

Client sends actions via HTTP. Server broadcasts state changes via WS.
This makes reconnection trivial — fetch current state via HTTP, resubscribe to WS.

### Database

SQLite via `bun:sqlite`. WAL mode. Foreign keys on.

Tables: `sessions`, `actors`, `scenes`, `events`

Actors stored as id + template name + JSON blob of serialized aspects.
Do not normalize aspects into columns — schema varies per template and content pack.

Event log (`events` table) is append-only. Never update or delete rows.
Server diagnostic logs go to stdout only — not the database.

### Logging

Two separate concerns:

**Game event log** → `events` table. Automatic via `@LogEvent` decorator on WS handlers.
Content authors never call this manually.

**Diagnostic log** → stdout only. Thin shim over `console.log` — no logging library.

```ts
// src/lib/Logger.ts — the entire implementation
const level = Bun.env.LOG_LEVEL ?? "info";

export const logger = {
  info: (...args: any[]) => console.log("[INFO]", ...args),
  warn: (...args: any[]) => console.warn("[WARN]", ...args),
  error: (...args: any[]) => console.error("[ERROR]", ...args),
  debug: (...args: any[]) => level === "debug" && console.debug("[DEBUG]", ...args),
};
```

`info`: lifecycle (server start, content loaded, WS connect)
`warn`: recoverable oddities (unknown WS command, content load error)
`error`: should-never-happen (unhandled exception, DB failure)
`debug`: everything else, off by default

If structured logging is ever needed (log aggregation, production deployment at scale),
swap the internals of `Logger.ts` for pino. Call sites are identical, nothing else changes.

### Simulation

Headless scenario runner for testing game logic. Same aspects/events/handlers as live session,
events captured to log instead of broadcast over WS.

```ts
const sim = new Simulation(encounterPrefab);
const result = await sim.run(async (scope, log) => {
  // drive combat manually, inspect state
});
console.log(result.events);
```

Accessible via CLI: `sim run ./scripts/encounter.ts`
This is the primary testing harness for game logic. More useful than unit tests here.

---

## Frontend Architecture

### View Protocol

Backend sends **semantic data with a type tag**, not layout instructions.
Frontend decides presentation. Backend describes WHAT, not HOW.

```ts
// backend sends
{ type: 'AbilityScores', data: { scores: {...}, modifiers: {...} } }

// frontend maps type → component
const ViewTypes = {
  AbilityScores: AbilityScoreTable,
  Skills: SkillList,
}
```

`DisplayColumn` and other layout views from the original implementation are removed —
layout is the frontend's job.

### Custom Spell UI — Override Registry

For spells/actions with bespoke UI (more compact than the generic renderer),
the client maintains an override registry keyed by spell id:

```ts
// client/src/lib/overrides/index.ts
import HighIdentifyUI from "./spells/HighIdentify.svelte";

export const SpellUIOverrides: Record<string, Component> = {
  high_identify: HighIdentifyUI,
};
```

Auto-populated via `import.meta.glob('./spells/*.svelte', { eager: true })` if preferred.
Filename convention: `HighIdentify.svelte` → key `high_identify`.

Generic renderer used for everything not in the registry. Override is pure opt-in.

Override components receive `{ spell, actor, oncast, oncancel }` and call `oncast(inputs)`
to submit through the same `cast` method. UI is bespoke, contract is typed and shared.

### Client-side Content Loading

```ts
// vite resolves this at build time — pattern must be a literal
const modules = import.meta.glob("/content/**/*.svelte", { eager: true });
```

Server content (logic) can live anywhere at runtime.
Client content (UI overrides) must live under a known root at build time.

### Hosting

- GitHub Pages: static landing page only. "Enter server IP" form. No game UI.
- Bun server: serves SvelteKit static build + API + WS on single port.
- Player navigates to `http://<server-ip>:<port>` — gets real game client from Bun.

Dev: SvelteKit on 5173, proxies `/api` and `/ws` to Bun on 3000.
Prod: everything on one port, `adapter-static` build served by Bun.

---

## Milestone 1 — Backend + SRD + CLI (Current)

**Goal:** Full combat encounter runnable from CLI. No frontend.

**SRD Scope (deliberately limited):**

- Classes: Fighter (Champion), Rogue (Thief), Cleric (Life), Wizard (Evocation)
- Races: Human (variant), Elf (High), Dwarf (Hill), Halfling (Lightfoot)
- Spells: Cantrips + levels 1–3, Wizard and Cleric lists only
- Items: SRD weapon table, all armor — no magic items
- Conditions: the common 8 (prone, poisoned, incapacitated, restrained, grappled,
  frightened, paralyzed, unconscious)
- Combat: action economy, death saves, short/long rest, concentration

**Done when:**

- Fighter vs Goblin encounter resolves entirely via CLI
- `sim run` executes headless combat script and prints event log
- All serialization round-trips correctly
- `bun test` passes for core primitives

---

## Milestone 2 — Web Frontend

**Goal:** Full character sheet, character creation, DM controls. No board yet.

Character creation is the hard part — multi-step wizard with interdependent
selections (class → features → proficiency choices → sheet preview).
Plan a full week just for this.

WS state sync architecture and reconnection handling should be designed carefully
before building, not after. The session context flowing through the Svelte component
tree needs to be right from the start.

---

## Milestone 3 — Game Board (Future)

**A scene is a view on a session, not a separate data store.** All persistent state
lives on the session (campaign). Scenes are lenses over that state — they can display
it differently (board vs no-board, cutscene vs combat) but they never own it.
Transient scene data (animation state, UI cursor position) never enters the session.
Any scene implementation is therefore orthogonal to the existing actor/aspect work.
Turn order is federated by the active scene — the engine has no global turn concept.

Board is a view over actor state, not a separate system. The same combat mechanics
work with or without a board — board is additive, not a replacement.

**Key decisions to make before building:**

- Canvas vs SVG. Canvas for performance/animations. SVG for simpler hit testing and
  DOM integration. Don't let this be decided by whatever gets generated first.
- Fog of war scope. It's genuinely complex (polygon clipping for line of sight).
  Ship a functional ugly board first — no fog of war in first pass.
- Start with: snap-to-grid tokens, click to move, distance display. That's it.

**Distance:** Euclidean vs Chebyshev vs taxicab — make this configurable per session.
5e uses Chebyshev (diagonals cost 1) by default but variant rules use Euclidean.

**Prefab stamping** is the mechanism for placing encounter groups on the board.
Local IDs in prefab definition → resolution pass at stamp time → absolute session UUIDs.
The `actor_groups` table tracks which actors came from the same stamp for collective ops.

---

## Milestone 4 — Assets + Custom Scenes (Future)

**Assets:** Store as string references (filenames/relative URLs) in scene JSON blobs.
Bun serves `/assets` as a static directory. No asset storage logic in the DB.

**Cutscene engine** is a mini-engine within the engine. A scripted sequence has its
own state machine: dialogue lines, speaker portraits, music cues, transition effects,
wait-for-input. Budget this at roughly a week of focused work for a basic version.

**Scope cutscenes before building.** "Done" for v1 means:
per-scene background, per-scene music, linear dialogue with portraits, scene transition.
Full branching dialogue and minigames are a separate project.

**Music system:** per-scene background track, crossfade on scene transition,
optional per-token sound effects. Audio context lives on the client — server just
sends asset references, not audio data.

---

## Roles and Permissions

Roles are not an engine concern. Identity (who is connecting) is handled at the
web layer, not the game engine. Three roles exist at the web app level:

- **Admin (DM):** full access to all actions and resources
- **Player:** access to actions scoped to their own actor
- **Spectator:** read-only

Permissions are modeled as "role has access to action X on resource Y." Implementation
is a decorator on HTTP/WS handlers — not baked into the engine or the actor model.
Build this during M2 when the web layer exists. Do not design it during M1.

Ownership (`player owns actor`) is just another decorator — orthogonal to the engine.
The engine never checks ownership. The web layer does.

---

## Content Error Handling

Content files load at startup via decorator side effects. Errors in content files
should **never crash the server.** The loader collects errors and reports them, then
continues with whatever loaded successfully.

```
[WARN] Failed to load content: content/classes/MyBrokenClass.ts
       TypeError: Cannot read property 'x' of undefined
[INFO] Content loaded: 4 classes, 3 races, 12 spells (1 error)
```

This matters because vibecoded content has a higher error rate. "Server crashes on
startup" is a painful feedback loop when iterating on content. Load what works, log
what didn't.

Content errors are `warn` level, not `error` — a missing homebrew class is not a
server failure.

---

## Unknown Aspect Fallback (Frontend)

When content defines a new aspect the frontend has no registered component for,
the view renderer needs a fallback rather than silent omission.

Policy:

- **Dev mode:** render unknown aspects as a labeled JSON dump. Visible, ugly, useful.
- **Prod mode:** omit silently.

This makes the content development loop faster — add an aspect, see its raw data
immediately without writing a component first. Write the component when the shape
is stable.

Implemented as a catch-all in the view renderer after the `ViewTypes` lookup fails.

---

## Design Constraints (read before adding dependencies)

These constraints are intentional and should not be worked around without good reason:

- **Minimal dependencies.** Bun + Hono + SvelteKit + SQLite + pino. Each additional
  dependency is a future maintenance burden when picking this up after months away.
  If a problem can be solved with ~50 lines of TypeScript, do that instead.

- **Less code is better.** Implementations should read like proofs — declarative,
  DRY, no incidental complexity. If something takes more code than feels right,
  the abstraction is probably wrong.

- **Web browser only.** No Electron, no mobile bundling. Browser is the only client.
  This constraint is permanent.

- **Latency is not a concern.** All interactions are turn-based or low-frequency.
  First write to server wins. No optimistic UI required. No real-time constraints.

- **DRY across the network boundary.** All shared types live in `packages/core`.
  Nothing is duplicated between server and client. If a type exists in both places,
  it belongs in core.

---

## Things That Were Considered and Rejected

- **Phase/lifecycle on Actor** — unnecessary. Engine runs or it doesn't.
  Missing aspect is a content bug, not a recoverable state.

- **Input schema DSL** — deferred to P2. Content authors write typed `cast()` signatures.
  Generic renderer from Input declarations is a quality-of-life feature, not core.

- **Slot abstraction** — redundant with the type system. Let TypeScript handle it.

- **Server-driven layout (DisplayColumn etc)** — removed from view protocol.
  Backend describes data, frontend owns layout.

- **Glob-based content scanning** — rejected as primary mechanism. Explicit `index.ts`
  entry point is idiomatic. Glob as optional convenience for users who want it.

- **ECS for core model** — wrong problem. ECS solves runtime add/remove of components
  for simulation actors. You want static typed aspects with serialization.

- **OT/CRDT for conflict resolution** — massively overengineered for D&D.
  Last-write-wins with delta + idempotency keys is sufficient.
  The human at the table resolves the rare real conflict verbally.
