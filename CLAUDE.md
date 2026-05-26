# Atelier

A self-hosted D&D 5e tabletop app. Single Bun server, SvelteKit frontend, SQLite
database. Stack: Bun + Hono + SvelteKit + Tailwind + `bun:sqlite`, TypeScript strict mode
with decorators, Bun-workspaces monorepo (`packages/core`, `packages/server`,
`packages/client`). Designed for personal use — there is only one user, but ergonomics and
clean abstractions are not to be compromised on that account.

---

## How this documentation is organized

This file is a **router**, not a summary. It holds only the invariants that govern *every*
change. Everything else lives in a subsystem file under `docs/`; load the one you need.

| File | Load when |
|------|-----------|
| `ENV.md` | Setting up the project, touching build config, adding a package, or debugging imports/aliases. |
| `CORE.md` | Touching anything in `packages/core`, or any content that contributes to stats, serializes, or references other composites. |
| `SERVER.md` | Working on the server, WS/HTTP handlers, the DB, content loading, or simulation. |
| `FRONTEND.md` | Working on the client. |
| `PLAN.md` | Planning future milestones (rarely needed during implementation). Also holds the record of rejected alternatives. |

Each file opens with a one-line statement of when to load it, mirroring the table above.
This table is the single source of truth for *which file do I need* — answer that question
from here without opening anything else.

---

## Invariants (these govern every file)

These are the cross-cutting laws. They constrain code everywhere, so they live here
permanently rather than in any one subsystem file.

- **Everything is a composite by default.** Capabilities — identity, serialization,
  structure, contribution to stats — are aspects attached to a composite, not intrinsic
  properties of bespoke classes. The burden of proof is inverted: a thing is a composite
  unless there is a *specific* reason it should not be. "This doesn't need a composite" is
  not a reason. A non-composite living in a composite-everything system is the anomaly that
  breaks uniformity, and that cost is usually larger than the local cost of the composite.
  Reserve non-composite typed values for leaves with no independent capability surface. (Full
  rationale in `CORE.md`.)

- **Missing aspect = content bug = thrown error.** There is no phase or lifecycle guard. The
  engine runs or it doesn't. `get` throws with a clear message if an aspect is absent; this
  is intentional, not a gap to defend against.

- **DRY across the network boundary.** All shared types live in `packages/core`. Nothing is
  duplicated between server and client. If a type exists in both places, it belongs in core.

- **Minimal dependencies; less code is better.** Bun + Hono + SvelteKit + SQLite (+ pino if
  structured logging is ever needed). Each dependency is a future maintenance burden when
  picking this up after months away. If a problem can be solved with ~50 lines of TypeScript,
  do that instead. Implementations should read like proofs — declarative, DRY, no incidental
  complexity. If something takes more code than feels right, the abstraction is probably wrong.

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
  rewrites every piece of content. Everything *behind* such a contract (ordering strategy,
  fixed-point vs. single-pass, etc.) stays deferred. The test: a change is worth committing to
  early only if adding it later would be a breaking change for content — optional parameters
  and optional fields with defaults are always safe to defer.
