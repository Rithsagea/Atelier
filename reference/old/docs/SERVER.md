# SERVER — Server Architecture

_Load when: working on the server, WS/HTTP handlers, the DB, content loading, or simulation._

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

### SRD is homebrew

SRD content is treated as homebrew: it is built as an extension of the base engine using the
same primitives, not as a privileged built-in. The engine therefore never enumerates content
_kinds_ in advance — discovery is always by capability query — so that homebrew mechanics
extend the system exactly the way SRD content does.

### Defining Content

```ts
// content/classes/Fighter.ts
@Register(Classes)
export class Fighter extends Class {
  readonly name = "Fighter";
  readonly hitDie = 10;

  @Subscribe(LoadEffectsEvent)
  loadEffects(composite: Composite) {
    composite.get(SavingThrowsAspect).addProficiency("strength");
    composite.get(SavingThrowsAspect).addProficiency("constitution");
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
  cast(composite: Composite, inputs: { targetPoint: Point; slotLevel: number }) {
    composite.get(SpellSlotsAspect).consume(inputs.slotLevel);
    // apply damage to composites in range
  }
}
```

---

## Startup Sequence

```
1. import('./content/index.ts')     — populate TypeMaps, register NetworkEvents
2. runMigrations()                  — idempotent SQLite schema setup
3. registerHandlers(...)            — scan @ServerEvent metadata, wire WsRouter
4. Hono app.listen(port)            — REST + WS + static client files
5. cli.listen()                     — concurrent on stdin
```

---

## WS Architecture

`@ServerEvent` decorator registers handler methods on the `WsRouter` singleton.
`WsRouter.dispatch` deserializes payload and calls the handler.
`SessionRegistry` is in-memory (transient), holds live WS connections, handles broadcast.

**Critical split — HTTP vs WS:**

- HTTP: request/response shaped operations (load sheet, create composite, cast spell action)
- WS: push/broadcast only (HP changed, scene advanced, chat, dice results broadcast)

Client sends actions via HTTP. Server broadcasts state changes via WS.
This makes reconnection trivial — fetch current state via HTTP, resubscribe to WS.

---

## Database

SQLite via `bun:sqlite`. WAL mode. Foreign keys on.

Tables: `sessions`, `composites`, `scenes`, `events`

Composites stored as id + template name + JSON blob of serialized aspects.
Do not normalize aspects into columns — schema varies per template and content pack.

Event log (`events` table) is append-only. Never update or delete rows.
Server diagnostic logs go to stdout only — not the database.

---

## Logging

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

---

## Simulation

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
