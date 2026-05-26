# PLAN — Milestones & Design Record

_Load when: planning future milestones (rarely needed during implementation). Also holds the
record of rejected alternatives._

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
Any scene implementation is therefore orthogonal to the existing composite/aspect work.
Turn order is federated by the active scene — the engine has no global turn concept.

Board is a view over composite state, not a separate system. The same combat mechanics
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
The `composite_groups` table tracks which composites came from the same stamp for collective ops.

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

## Roles and Permissions (M2)

Roles are not an engine concern. Identity (who is connecting) is handled at the
web layer, not the game engine. Three roles exist at the web app level:

- **Admin (DM):** full access to all actions and resources
- **Player:** access to actions scoped to their own actor
- **Spectator:** read-only

Permissions are modeled as "role has access to action X on resource Y." Implementation
is a decorator on HTTP/WS handlers — not baked into the engine or the actor model.
Build this during M2 when the web layer exists. Do not design it during M1.

Ownership (`player owns composite`) is just another decorator — orthogonal to the engine.
The engine never checks ownership. The web layer does.

---

## Things That Were Considered and Rejected

(Two rejections that justify core abstractions — event-based stat contribution, and the
maintained contributor list — live in `CORE.md` instead, next to the abstractions they
explain.)

- **Phase/lifecycle on Composite** — unnecessary. Engine runs or it doesn't.
  Missing aspect is a content bug, not a recoverable state.

- **Input schema DSL** — deferred to P2. Content authors write typed `cast()` signatures.
  Generic renderer from Input declarations is a quality-of-life feature, not core.

- **Slot abstraction** — redundant with the type system. Let TypeScript handle it.

- **Server-driven layout (DisplayColumn etc)** — removed from view protocol.
  Backend describes data, frontend owns layout.

- **Glob-based content scanning** — rejected as primary mechanism. Explicit `index.ts`
  entry point is idiomatic. Glob as optional convenience for users who want it.

- **ECS for core model — RECONSIDERED AND ADOPTED.** This was originally rejected on the
  grounds that "ECS solves runtime add/remove of components for simulation entities; you want
  static typed aspects with serialization." That reasoning has been deliberately reversed: the
  composite-substrate model (everything is a composite, capabilities are aspects, discovery is
  by capability query at every level) is an ECS-style pattern, adopted because the _uniformity_
  it buys — one substrate, one discovery primitive, one extension mechanism — is worth the cost
  of giving up some static-type legibility (recovered where needed via StructureAspect and
  content-author view wrappers). This note is kept rather than deleted so the reversal is on the
  record; the original concern about serialization is addressed by the transient-by-default
  purity law in `CORE.md` (transient state is a pure function of persisted state).

- **OT/CRDT for conflict resolution** — massively overengineered for D&D.
  Last-write-wins with delta + idempotency keys is sufficient.
  The human at the table resolves the rare real conflict verbally.
