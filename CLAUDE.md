# Atelier

A self-hosted D&D 5e engine. Node + `node:sqlite`, TypeScript strict mode, npm-workspaces
monorepo (`core`, `server`, `playground`). The current target is a headless, single-user MVP
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

- **DRY across the network boundary.** All shared types live in `core`. Nothing is
  duplicated between server and client. If a type exists in both places, it belongs in core.

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

- **Reserve future-proofing for content-facing contracts only.** The exception to the above
  is the small set of signatures that all content is written against (e.g. a stat
  contribution carrying a thunk rather than a number, a contributor receiving its owning
  composite). These are worth a small generality tax up front because retrofitting them later
  rewrites every piece of content. Everything _behind_ such a contract (ordering strategy,
  fixed-point vs. single-pass, etc.) stays deferred. The test: a change is worth committing to
  early only if adding it later would be a breaking change for content — optional parameters
  and optional fields with defaults are always safe to defer.
