# PLAN — MVP: headless D&D game engine

Draft. Supersedes the milestone sections of `reference/old/docs/PLAN.md`; the rejected-alternatives
record there still stands unless noted.

---

## Goal

A **headless engine with an internal client** that can create, manage, and play a D&D 5e game.
No frontend, no board, no HTTP, no network at all: the client is a plain in-process interface
that reads and writes the game directly. A WebSocket layer will later wrap this same client
(see _Client_). There is exactly **one user, who manages everything**: creates the game, builds
every character and monster, runs the encounter, and takes every turn. No seats, roles, or
ownership. For the MVP that user is a **dummy driver**: a script or policy-driven bot that calls
the client in the same process.

The point of the MVP is to prove the engine, not the product: composites/aspects, event-based
stat contribution, serialization, persistence, and an action loop that can run a full encounter
unattended and reproduce it exactly.

### Non-goals

- UI of any kind (the old M2–M4: frontend, board, assets, cutscenes).
- HTTP, WebSockets, or any transport. The client is called in-process; the WebSocket wrapper is
  deferred.
- Users, seats, roles, ownership, authentication. The client trusts its caller.
- Runtime content loading. Content is code, loaded at startup; changing it means a restart.
- Content breadth. One class, one race, one monster, a handful of items (see _Rules slice_).

---

## Definition of done

One scenario runs end-to-end through the client with no human input, and is reproducible:

1. Create a game; create two Fighters (Champion) and two Goblins.
2. Start an encounter with all four.
3. Initiative rolls; turns advance; the driver attacks, moves, and ends turns for whichever
   composite is active, via the client.
4. Encounter ends when one side is down. The driver closes it.
5. Close and reopen the game store mid-encounter; state resumes exactly from the database.
6. Replaying the stored log into a fresh game with the same initial seed yields an identical
   final state.

`npm test` covers core primitives and the scenario above (the scenario test drives the real
client).

---

## Architecture

Five workspaces, all Node, run through `tsx` (see open decision 1). `server` does not exist yet;
it is created when persistence (M3) needs a home.

| Package      | Role                                                                                    |
| ------------ | --------------------------------------------------------------------------------------- |
| `core`       | Generic plumbing, no D&D: composites, aspects, structure, serialization, events, util   |
| `game`       | D&D-specific engine: abilities, skills, `Sheet`, the rules, and the lists content fills |
| `content`    | Concrete SRD and user content (species, feats, ...), registered into `game`'s lists     |
| `server`     | Persistence (`node:sqlite`), the live game registry, and the client; the authority      |
| `playground` | Dummy-driver harness: bot policies that call the client; runs the scenario              |

`core`, `game`, and `content` stay free of Node APIs so a future client can import them (DRY
across the network boundary). `game` depends on `core`; `content` depends on both; `server`
depends on all three and imports each `content` index at startup, since content registers by
being imported. The client's request and
result types live in `core` (generic) or `game` (D&D-specific), never in `server`, so the future
WebSocket wrapper and any remote caller share them.

### Invariants carried over from CLAUDE.md

Everything is a composite; missing aspect = thrown content bug; no speculative methods;
minimal dependencies.

### Domain model

- **Game** — the root composite (old "session"): a **memoryless snapshot** of the game's current
  state — its composites, encounter, status, and RNG state. It holds no history and no log;
  everything that records how it got here lives in the server layer (see _Game module_).
- **Composite** — characters, monsters, and the game itself are all composites built from a
  `Structure`. Capabilities are aspects:
  `Id`, `Name`, `BaseAbilityScore`, `AbilityScore` (computed), `HitPoints`, `ArmorClass`
  (computed), `Equipment`, `Actions`/`ActionEconomy`, `Initiative`, `Conditions`, `Holder`.
- **Encounter** — a composite aspect of the game that federates turn order. The engine has no
  global turn concept; the active encounter owns it (carried over from the old M3 note).
- **Log entry** — a server-layer, append-only record of an applied action (what, inputs,
  result). It is the audit trail and the replay source, and is never part of a `Game`.
  (Distinct from engine events such as `LoadEffectsEvent`, which are in-memory dispatch and
  never persisted.)

Stat contribution is a `Contributor<T>` (`core/src/composite/Contributor.ts`): `apply(target,
owner)` writes to a mutable `T` (an `AbilityScore`, a `SkillProficiency`, ...) given the owning
composite, and runs at refresh time against the owner's current state. `contribute(key, owner,
target)` walks the owner's descendants through each composite's own `Holder`. Computed aspects
are transient and rebuilt by `refresh(sheet)` (`game/src/sheet/Sheet.ts`), which runs the
projections in dependency order.

**Aspects are final or variant.** `@Aspect.Final(id)` is a class that is its own aspect;
`@Aspect.Variant(Key, id)` registers a class into a `RegistryKey` slot (`Species`, `Background`,
`Feat`). `game` defines the keys and rules; `content` fills the slots (see _Architecture_).

### Game module

Three layers, each importing only downward:

| Layer      | Lives in                      | Owns                                                                       |
| ---------- | ----------------------------- | -------------------------------------------------------------------------- |
| **Engine** | `core` (composite, serial, …) | Composites, aspects, structure, serialization, events. Knows no D&D.       |
| **Game**   | `game`                        | The objects the user interacts with, and the rules over them.              |
| **Server** | `server` (client, db)         | The client, persistence, and all history (event log, `seq`, initial seed). |

The game module is **plain JS objects** at the surface: a `Game` (a composite, see the decision
below) serializes to JSON data with no methods, so it can be stored, sent over the wire later,
and replayed as-is. Game-specific behavior is free functions over that data. Split by what depends
on Node:

- `game` — the object shapes (`Game`, encounter status) and the pure rules over them,
  such as whether an action is legal right now. No Node APIs, so a future client imports the
  same definitions.
- `server` — the live side: loading a `Game` from the database, the in-memory
  registry of active games, and applying engine results to a `Game` before they are persisted.

Shape sketch (illustrative, not final):

```ts
interface Game {
  id: string;
  rng: number; // current PRNG state, so a restored snapshot rolls the same dice
  status: "active" | "closed";
  encounter?: { order: string[]; active: number };
}
```

Composites are not nested in this sketch; they are reached through the game's `Holder` aspect.

What the game objects must serve (single user, so no one is ever refused for who they are):

- **Setup:** create a game; create characters and monsters into it.
- **Turn gating:** only the active composite may act or end turn. This is a rule of the
  encounter, not an authorization check; out-of-turn actions throw a `GameError`.
- **Lifecycle:** `active → closed`; a closed game rejects actions without the engine knowing.
- **Direct editing:** the user may set HP or conditions outright, logged like any other change.
- **Summary view:** `getGame` returns the `Game` snapshot nearly as-is.
- **Driving by bot:** a bot is just a caller; nothing in the game layer distinguishes it from a
  human at a terminal.

**Decision: `Game` stays a composite.** CLAUDE.md makes composite the default, so `Game` is
built from a `Structure` like everything else, and its serialized form is the plain object
sketched above. Its fields are aspects (encounter, status, RNG state), not bespoke class
members. Revisit only if a concrete need forces it.

---

## Client

The client is the **only way anything reads or writes a game**. It is an object in `server`
whose methods operate directly on the game and the database: no routing, no serialization
boundary, no auth. The dummy driver and the tests are its callers; the later WebSocket layer
is a thin wrapper that forwards messages to the same methods.

Surface (names illustrative; the shape is the contract):

| Method                               | Purpose                                          |
| ------------------------------------ | ------------------------------------------------ |
| `createGame(seed?)`                  | Create a game; returns its snapshot              |
| `getGame(id)`                        | The `Game` snapshot                              |
| `events(id, after?)`                 | The log since `seq`, for following what happened |
| `createCharacter(id, spec)`          | Create a character (class/race/abilities)        |
| `createMonster(id, name)`            | Instantiate a monster template                   |
| `getComposite(id, cid)`              | Serialized composite                             |
| `editComposite(id, cid, patch)`      | Direct edit (HP, conditions), logged             |
| `startEncounter(id, cids)`           | Participants in, initiative rolled               |
| `getEncounter(id)`                   | Order, active composite, per-turn economy        |
| `legalActions(id, cid)`              | Legal actions for this composite right now       |
| `act(id, { actor, action, inputs })` | Perform an action                                |
| `endTurn(id)`                        | End the active composite's turn                  |
| `closeEncounter(id)`                 | Close the encounter                              |

Failures are thrown as a `GameError` with a stable string `code` (`out-of-turn`,
`game-closed`, `illegal-action`, …) so a wrapper can forward them without parsing messages.

`legalActions` exists so the driver needs no rules knowledge: it asks what is legal and
chooses. Actions are content classes with a typed `inputs` argument (no input-schema DSL yet),
validated by the action itself.

**Built to be wrapped.** The WebSocket layer is deferred, but three properties are cheap now
and breaking to retrofit, so they hold from the start:

1. **Methods are `async`.** A network hop is asynchronous; changing sync to async later
   rewrites every caller.
2. **Arguments and results are plain JSON-serializable data.** No class instances, no
   callbacks, no live references.
3. **Reads return snapshots, never live composites.** Callers cannot mutate game state except
   through a write method, which is what lets the wrapper mirror the client exactly.

Writes are serialized, one at a time. There is no concurrent-writer story because there is one
writer. Push (broadcasting new log entries to subscribers) is the WebSocket layer's job; the
log it would fan out already exists.

---

## Rules slice

Deliberately minimal, built **as homebrew on the base engine** — no privileged built-ins.

- **Class:** Fighter (Champion), levels 1–3 only. Fighting style, Second Wind, Action Surge,
  Improved Critical.
- **Species:** Human. **Background:** Soldier. (Terms follow SRD 5.2.1; see _Minimum player
  character_.)
- **Monster:** Goblin (Nimble Escape optional).
- **Items:** longsword, shortbow, chain mail, shield.
- **Combat:** initiative, action / bonus action / movement economy (abstract distance, no
  board), attack roll vs AC, advantage/disadvantage, damage and crits, 0 HP → unconscious → dead
  (death saves), short rest. No spells, no concentration, no general conditions beyond
  `unconscious`/`dead`.
- **Dice:** a seeded PRNG whose current state lives in the `Game` snapshot. All randomness goes
  through it; the initial seed (server metadata) plus the logged actions are what make replay
  deterministic.

Skills and saving throws are computed aspects as already modelled in `reference/old`.

---

## Minimum player character

The smallest character that is _complete_: it can be built from a spec, has every number a
level 1 sheet needs, and can take part in the encounter. Rules text is SRD 5.2.1 (2024), kept
locally in `scratch/dnd-5e-srd-markdown/` and not committed; its terms apply (_species_ not
race, _background_ supplies ability increases and an Origin feat). Section names below point
into it.

**The reference character:** level 1 **Human Fighter** with the **Soldier** background, using
the Standard Array. Everything below is the minimum needed to build and play exactly this
character. Anything not listed is out of scope.

### Sheet (aspects on the character composite)

| Aspect                     | Contents                                                                            | Source                      |
| -------------------------- | ----------------------------------------------------------------------------------- | --------------------------- |
| `Id`, `Name`               | identity                                                                            |                             |
| `BaseAbilityScore`         | six scores, assigned from the Standard Array (15, 14, 13, 12, 10, 8)                | Character Creation, step 3  |
| `Level`                    | class level (1); proficiency bonus is derived from it (+2 at levels 1–4)            | Fighter Features table      |
| `AbilityScore` (comp.)     | base + background increase (+2/+1, or +1/+1/+1); modifier is `floor((score−10)/2)`  | Adjust Ability Scores       |
| `SkillProficiency` (comp.) | set of proficient skills; contributors add to it, expertise is deferred             | Fill In Numbers             |
| `SkillScore` (comp.)       | the 18 skills: ability modifier + proficiency bonus if in `SkillProficiency`        | Fill In Numbers             |
| `SavingThrow` (comp.)      | six saves; class grants Strength and Constitution proficiency                       | Core Fighter Traits         |
| `HitPoints`                | max, current, temporary; level 1 max is die + Constitution modifier (d10 → 10 + C)  | Level 1 Hit Points by Class |
| `HitDice`                  | die size and spent count; 1 die at level 1                                          | Fill In Numbers             |
| `DeathSaves`               | successes and failures                                                              | Death Saving Throws         |
| `ArmorClass` (comp.)       | base from armor or 10 + Dexterity modifier, plus Shield, plus contributions         | Equipment, Armor            |
| `Initiative` (comp.)       | Dexterity modifier plus contributions                                               |                             |
| `Speed`                    | 30 feet (abstract distance)                                                         | Human                       |
| `Equipment`                | inventory; equipped armor, shield, and weapons                                      | Starting Equipment          |
| `Attacks` (comp.)          | per weapon: attack bonus (ability modifier + proficiency), damage dice and type     | Fill In Numbers             |
| `Resources`                | named use-pools with a recharge rule (short, long) — see _Supporting systems_       | Second Wind, Long Rest      |
| `Species`                  | one registry slot (variant): species traits such as Human's Skillful                | Character Species           |
| `Background`               | one registry slot (variant) of five parts: abilities, feat, skills, tool, equipment | Character Backgrounds       |
| `Feats`                    | list of feat composites, each a variant of `Feat`                                   | Feats                       |
| `Class`                    | not built yet: one registry slot (variant) for the class and its features           | Core Fighter Traits         |
| `HeroicInspiration`        | boolean, held by the character                                                      | Rules Glossary              |
| `Conditions`               | `unconscious`, `dead` only                                                          | Rules slice                 |

Class features, species traits, and feats are **composites**, not flags on the sheet. Each is a
feature composite attached through `Holder` that contributes to the owning character through
the same event-based stat contribution as everything else (CLAUDE.md, composite-by-default). The
sheet never checks "does this character have Defense?"; the Defense feature contributes +1 to
`ArmorClass` while armor is worn.

### The character's content

- **Fighter level 1:** d10 Hit Die; Strength and Constitution saves; two skills from the class
  list; all weapons and armor plus Shield; _Fighting Style_ (feat: **Defense**, +1 AC while
  wearing armor); _Second Wind_ (below). _Weapon Mastery_ is out of scope.
- **Human:** Medium, 30 feet; _Resourceful_ (below); _Skillful_ (one skill of choice);
  _Versatile_ (one Origin feat of choice: **Alert**, which adds the proficiency bonus to
  Initiative).
- **Soldier:** +2/+1 to Strength, Dexterity, or Constitution; Origin feat **Savage Attacker**
  (once per turn, roll a weapon's damage dice twice and use either); Athletics and Intimidation;
  the gaming-set tool is recorded as a name with no mechanics.
- **Equipment:** chain mail (AC 16), shield (+2 AC), longsword, shortbow, arrows. Chosen
  directly, not through the Class's "choose A, B, or C" packages. No gold or encumbrance.

Out of scope for the minimum: languages, alignment, personality and backstory fields, tool
mechanics, experience points and leveling, multiclassing, spellcasting, weapon mastery, ability
score rolling or point buy (see `BaseAbilityScore` in `reference/old` for the aspect shape).

### Supporting systems

What the sheet above needs underneath it. Each is a rule over composites, not a new kind of
object.

1. **d20 tests.** Ability checks, saving throws, and attack rolls: d20 plus modifier, with
   advantage and disadvantage, critical hits (natural 20), and Heroic Inspiration reroll.
2. **Dice.** One seeded PRNG in the `Game` snapshot (Rules slice); damage dice, the d20, and
   "roll twice" (Savage Attacker) all draw from it.
3. **Stat contribution.** `Contributor<T>`s write to ability scores, skill proficiency, AC,
   Initiative, and attack/damage, so features compose without the sheet knowing about them.
4. **Action economy.** Per-turn action, bonus action, and movement, reset when a turn starts;
   `legalActions` reads it.
5. **Resources.** A use-pool aspect (`current`, `max`, recharge `short | long`) that features
   own and rests reset. Second Wind is the first user.
6. **Rests.** Short and long rest as **actions** performed through `act`, not new client
   methods. A short rest lets the character spend Hit Dice and recharges short-rest resources; a
   long rest restores Hit Points, recharges everything, and fires a "long rest finished" event
   that Human _Resourceful_ listens for.
7. **Hit points.** Damage, healing clamped to the maximum, temporary HP, 0 HP → unconscious →
   death saves → dead (already in the Rules slice).
8. **Equipment rules.** Weapon records (damage dice, damage type, melee/ranged, Versatile) and
   armor records (base AC, Dexterity cap); AC is a computed aspect over what is equipped.
9. **Character builder.** `createCharacter(id, spec)` (Client) takes
   `{ name, class, species, background, abilities, skills, ... }`, validates it against the
   steps above, and returns the composite. It is the only place that knows the creation order.

### Two player interactions

One from the class and one from the species. Between them they exercise the action economy,
resources, rests, dice, HP, and the one genuinely new mechanism, a mid-action decision.

**1. Second Wind (Fighter).** Bonus action: regain `1d10 + Fighter level` HP, up to the maximum.
Two uses; a short rest restores one, a long rest restores both.

- Legal only when it is the character's turn, the bonus action is unspent, and a use remains.
- Exercises: bonus action, resource pool, a die roll, HP clamp, short and long rest recharge,
  and `legalActions` hiding it when it is not available.

**2. Heroic Inspiration (Human _Resourceful_).** Finishing a long rest sets
`HeroicInspiration`. The player may expend it to reroll any die they just rolled and must keep
the new result. A character that already has it gains nothing.

- Exercises: rest-triggered events, a character-held flag, and a **decision after a roll**. The
  action that produced the roll cannot complete until the player chooses keep or reroll.
- This requires the game to suspend an action and wait. See open decision 6.

**Scenario tests:** (a) a damaged Fighter uses Second Wind twice, is refused a third time,
short-rests, and uses it again; (b) a Human finishes a long rest, attacks, rerolls the attack,
and the reroll replays identically from the same seed.

---

## Persistence

SQLite via `node:sqlite`, WAL, foreign keys on. Migrations are an idempotent `CREATE IF NOT
EXISTS` pass at startup.

- `games` — id, created_at, initial seed, `seq` (log cursor), JSON snapshot of the `Game`'s own
  aspects
- `composites` — id, game, template name, JSON blob of serialized aspects (not normalized:
  schema varies per template)
- `events` — game, seq, type, payload JSON; the log. Append-only, never updated or deleted.
  Server metadata: nothing in `core` or the game module reads it.

State is always the latest snapshot; history is always the log. A state change and its log
append happen in **one transaction**. On restart the client reopens the store and loads the snapshot and composites
and continues from the stored `seq`; replay is a separate path that re-applies logged actions
to a fresh game built from the initial seed.

---

## Dummy driver

`playground` holds bot policies and runnable scenarios that call the client in-process.

- `Bot` — a policy `(state, legalActions) => action`, applied to whichever composite is
  active. Two to start: **aggressive** (attack the lowest-HP enemy, otherwise move closer) and
  **random** (uniform over legal actions, seeded).
- `scenario` — builds the game, runs the encounter to completion under a seed and a turn cap,
  and prints the event log and a final-state digest. The scenario test asserts the digest is
  stable across runs.

The random bot is a cheap fuzzer: any run that throws, deadlocks, or violates an invariant
(negative HP, two active turns) is a bug in the engine or the rules.

---

## Milestones

Each milestone ends with something runnable and tested.

**M0 — Foundation.** Settle the open decisions below. Workspaces, lint, format, test runner.
Port the reference primitives to Node: `BiMap`, `Constructor`, metadata, the serialization
strategies. _Done:_ round-trip serialization tests pass under the chosen test runner.

**M1 — Composites in `core`.** `AspectKey`, `Composite`, `Structure`, events, stat
contributions (`Contributor<T>`), computed `AbilityScore`/`SkillScore`. _Done:_ a `Sheet` structure
builds, serializes, deserializes, and recomputes derived stats identically.

**M2 — Rules in `game`, content in `content`.** _Progress:_ built: `Sheet` with ability scores,
`SkillProficiency`/`SkillScore`, `Initiative`, `Level`, `refresh`; Human (Skillful), Alert,
Soldier (abilities and skills only). Remaining: `HitPoints`/`HitDice`, `SavingThrow`,
`Equipment`/`ArmorClass`, `Attacks`, `Class` and Fighter, dice and the `Game`, actions. Fighter, Human, Goblin, items, the combat actions, seeded dice.
Run a full encounter as a pure function call — no client, no database. _Done:_ a scripted
Fighter vs Goblin fight resolves deterministically, the reference character from _Minimum
player character_ builds with the SRD's level 1 numbers, and both player interactions pass
their scenario tests.

**M3 — Persistence and the event log.** SQLite schema, repositories, transactional
write-and-append, replay. _Done:_ replaying a stored log into a fresh game reproduces state;
restart mid-encounter resumes.

**M4 — Client.** The client surface above, `GameError` codes for out-of-turn, closed-game and
illegal actions, and the three wrap-readiness properties. _Done:_ every client method is
exercised by tests, and results round-trip through `JSON.stringify`/`parse` unchanged.

**M5 — Dummy driver.** Both bots, the scenario runner. _Done:_ the definition-of-done
scenario passes, plus a fuzz run of the random bot over many seeds with no invariant
violations.

---

## Open decisions

Settle in M0, before anything depends on them.

1. **Decorators. Decided: legacy `experimentalDecorators`, run through `tsx`.** Nearly every
   class is annotated, so one line per field matters more than avoiding a loader; the old
   `Property.*` / `@Aspect` design and `Structure`'s imperative registration port unchanged.
   Rejected: standard TC39 decorators (needs a new `Structure` registration path and a
   `Symbol.metadata` polyfill) and explicit `static serial` registration (about twice the
   lines per field, with a silent-drop hazard). Risk: legacy decorators are frozen; every
   consumer of `core` (Vite, tests) must apply `experimentalDecorators` to core's files. `tsx`
   applies a tsconfig only to files matched by its `include`, so a script in one package that
   imports another compiles the other with _standard_ decorators and crashes; run `tsx` with
   `--tsconfig` pointing at the root `tsconfig.json` (which has no `include`).
2. **Extensionless imports. Decided:** `tsx` resolves them, so keep the old extensionless style.
3. **Test runner. Decided:** `node:test`, run as `node --import tsx --test`; tests are colocated
   (`Foo.test.ts`). Revisit when a frontend shares a Vite config.
4. **Type checking. Decided:** `npm run typecheck` (`tsc --noEmit` per package). TypeScript is
   pinned to `~6.0` because `typescript-eslint` does not yet support 7. Each package has its own
   `tsconfig.json` extending the root one, so tools that look up the nearest tsconfig per file
   (Vite, esbuild) apply `experimentalDecorators` to core's files.
5. **Typed `get`.** Whether `Structure` should constrain `get` to its declared aspects. Not
   needed for the MVP; do not build it speculatively.
6. **Mid-action decisions.** Heroic Inspiration lets the player reroll _after_ seeing a roll, so
   an action must be able to pause. Proposal: `act` returns a result with a `pending` prompt
   (the roll and the legal choices) and the `Game` records it as part of its snapshot; while it
   is set, `legalActions` offers only the choices, and answering is another `act`. This keeps
   the client surface unchanged, persists across a restart, and replays from the log. The
   alternative, declaring the reroll up front (`act(..., { rerollIfBelow })`), is simpler but
   wrong against the rules. Settle before M2 builds attack resolution, since both reactions and
   this reroll will use the same mechanism. Limit the first version to the attack roll.

---

## Deferred (post-MVP)

Multiple users: seats, DM/player roles, ownership of composites, bearer-token authentication,
per-role visibility (hiding monster HP), spectators, and stale-write protection between
concurrent clients; a WebSocket layer wrapping the client (request/response forwarding plus push of new log
entries); a frontend and character-creation wizard; the board and
distance rules; spells and concentration; asset serving; the cutscene engine; runtime content
loading; a generic input-schema DSL for actions.

## Risks

- **Decorator/loader choice** can force rework of `core` serialization — hence M0.
- **Determinism leaks.** Any `Math.random`, `Date.now`, or `Map`-order dependence in rules code
  breaks replay. Lint-ban `Math.random` in `server`; route time and dice through the game.
- **Client drift from the wrap-readiness properties.** One method that returns a live composite
  or takes a callback makes the WebSocket layer impossible to write mechanically. Test the
  JSON round-trip on every method's result.
- **Scope creep in the rules slice.** The goal is a working loop, not 5e coverage; a missing
  rule is acceptable, a missing seam is not.
- **Retrofitting users later.** Adding seats and ownership after the fact must stay a game-layer
  change (new aspects on `Game`, new policy functions, a check in the client or its wrapper). Keep composites,
  actions, and the log free of any user concept so that remains true.
