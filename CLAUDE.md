# Atelier

A self-hosted D&D 5e engine. Node + `node:sqlite`, TypeScript strict mode, npm-workspaces
monorepo (`core`, `game`, `content`, `server`, `playground`; `server` is not created yet). The current target is a headless, single-user MVP
driven by an in-process client; a WebSocket layer and a SvelteKit frontend come later. Designed
for personal use — there is only one user, but ergonomics and clean abstractions are not to be
compromised on that account.

The project is mid-rewrite. The previous Bun implementation is archived in `reference/old/`
(code in `reference/old/packages/`, design docs in `reference/old/docs/`); it is reference
material, not part of the build — do not import from it.

---

## How this documentation is organized

This file is a **router**, not a summary. It holds only the invariants that govern _every_
change. Load the file you need; answer _which file do I need_ from this table without opening
anything else.

| File                                       | Load when                                                                                       |
| ------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| `PLAN.md`                                  | Planning or implementing the MVP: scope, layers, client surface, persistence, milestones.       |
| `reference/old/docs/CORE.md`               | Touching composites, aspects, `Structure`, serialization, or stat contribution (old design).    |
| `reference/old/docs/SERVER.md`             | Working on content loading, WS/DB handlers, or simulation (old design; Bun- and HTTP-specific). |
| `reference/old/docs/ENV.md`, `FRONTEND.md` | Rarely: build config and client notes from the Bun-era design.                                  |

The `reference/old/docs/` files describe the **old** implementation. Where they conflict with
`PLAN.md` or this file, those win.

---

## Invariants (these govern every file)

These are the cross-cutting laws. They constrain code everywhere, so they live here
permanently rather than in any one subsystem file.

- **Everything is a composite by default.** Capabilities — identity, serialization,
  structure, contribution to stats — are aspects attached to a composite, not intrinsic
  properties of bespoke classes. The burden of proof is inverted: a thing is a composite
  unless there is a _specific_ reason it should not be. "This doesn't need a composite" is
  not a reason. A non-composite living in a composite-everything system is the anomaly that
  breaks uniformity, and that cost is usually larger than the local cost of the composite.
  Reserve non-composite typed values for leaves with no independent capability surface. (Full
  rationale in `reference/old/docs/CORE.md`.)

- **Missing aspect = content bug = thrown error.** There is no phase or lifecycle guard. The
  engine runs or it doesn't. `get` throws with a clear message if an aspect is absent; this
  is intentional, not a gap to defend against.

- **DRY across the network boundary.** All shared types live in `core` (generic) or
  `game` (D&D-specific). Nothing is duplicated between server and client. If a type exists in
  both places, it belongs in one of those.

- **Minimal dependencies; less code is better.** Node + `node:sqlite` today, SvelteKit for the later frontend (+ pino if
  structured logging is ever needed). Each dependency is a future maintenance burden when
  picking this up after months away. If a problem can be solved with ~50 lines of TypeScript,
  do that instead. Implementations should read like proofs — declarative, DRY, no incidental
  complexity. If something takes more code than feels right, the abstraction is probably wrong.

- **Game state is a memoryless snapshot; history lives in the server layer.** A `Game`
  holds only current state. The event log, sequence numbers, and initial seed are server
  metadata that nothing in `core` reads. State is the latest snapshot; history is the log.

- **The client is the only way in.** Everything reads and writes a game through the client
  (async methods, JSON-serializable arguments and results, snapshots never live objects), so a
  WebSocket layer can later wrap it mechanically.

- **Web browser only, and latency is not a concern.** No Electron, no mobile bundling — the
  browser is the only client, permanently. All interactions are turn-based or low-frequency:
  first write to server wins, no optimistic UI, no real-time constraints.

---

## Conventions (how to write any code here)

- **No speculative methods.** Only add a function when there is an immediate, concrete caller
  for it in the current changeset. "We'll need this later" is not a reason to write it now.
  The event system, serialization hooks, and aspect interactions will dictate the right shape
  when the time comes — don't guess ahead of that.

- **No `index.ts` files.** No barrel files and no `exports: ./src/index.ts`. Packages expose
  their modules through a subpath `exports` map (`"./*": "./src/*.ts"`), and callers import the
  module they use (`@atelier/core/composite/Composite`). The one exception is `content`, where
  an `index.ts` per category lists the content it registers (`content/src/feats/index.ts`);
  importing it is what registers that content.

- **Aspects are final or variant.** `@Aspect.Final(id)` declares a class that is its own aspect
  (one implementation, serialized as that class). `@Aspect.Variant(Key, id)` registers a class
  into a polymorphic slot declared as a `RegistryKey` (`Species`, `Feat`, `Background`),
  serialized with a `$type` tag. A plain `AspectKey` is never serialized: capabilities such as
  `Holder` and the contributor keys.

- **`core` is generic; D&D lives under `game`.** Anything specific to D&D (abilities, skills,
  `Sheet`, rules) goes in the `game` package (`game/src`). The rest of `core`
  (composites, aspects, structure, serialization, events, util) must make sense without D&D.

- **Concrete content lives in `content`.** SRD and user-authored content (species, feats,
  classes, monsters, items) is written in `content` and registered into lists that `game`
  defines (`Feat`, `Species`, ...). `game` holds the keys, rules, and engine; it never imports
  `content`.

- **No `eslint-disable` / `eslint-enable` comments.** When a rule fires, fix the code (better
  types, a cast, a restructure). If a rule is wrong for the project, change `eslint.config.js`,
  where the exception is visible in one place, instead of silencing it inline.

- **Reserve future-proofing for content-facing contracts only.** The exception to the above
  is the small set of signatures that all content is written against (e.g. a
  contributor that writes to a mutable stat, and receives its owning composite). These are worth a small generality tax up front because retrofitting them later
  rewrites every piece of content. Everything _behind_ such a contract (ordering strategy,
  fixed-point vs. single-pass, etc.) stays deferred. The test: a change is worth committing to
  early only if adding it later would be a breaking change for content — optional parameters
  and optional fields with defaults are always safe to defer.

---

## Reporting (how to summarize finished work)

End a task with a report in this shape. Keep each section to what changed the reader's picture;
skip a section when it is empty, and do not retell the steps taken.

- **Done** — one line saying what now exists, with the commit hash if one was made.
- **Changed** — bullets of files or areas, each with the _why_ when it is not obvious.
- **Verified** — what was run and its result (`npm test` count, `typecheck`, `lint`). Say plainly
  what was _not_ run.
- **Needs you** — decisions, surprises, or follow-ups that block or shape the next step. Also
  list anything uncommitted. This is the only section that asks the reader to act, so notes
  that need nothing from the reader (a test reworked for a type change, a cast, a cosmetic
  line width) belong in **Changed** or **Verified**, not here.

## Working agreements (how to carry out a task)

- **Decide, then say so.** When a choice is not the user's to make (naming within an existing
  pattern, file placement, ordering of steps, commit grouping), pick the recommended option,
  state it in one line, and continue. Ask only when the answer changes what gets built or
  contradicts something the user already decided. Do not ask "should I proceed?" after
  recommending a course of action.
- **Docs move with the code.** A change that makes `CLAUDE.md` or `PLAN.md` wrong updates them
  in the same changeset. Do not finish a task and then ask whether to update the docs. When a
  request conflicts with a rule in `CLAUDE.md`, follow the request and draft the amended rule
  alongside it.
- **Check the source before stating a rule.** SRD rules, existing code, and the docs in this
  repo are available to read. Look up the SRD text in `scratch/dnd-5e-srd-markdown/` before
  naming or classifying a rule, rather than recalling it.
- **One commit per session.** A session's work lands as a single commit, made at the end of the
  session once tests, typecheck, and lint pass. Do not split it by concern, and do not commit
  mid-session unless asked.
