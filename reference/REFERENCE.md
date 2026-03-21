# Reference Implementations
Not compiled. Not part of the build. Do not import from here.
Read these files when implementing the listed components.

## Core primitives
| File | Read when implementing |
|------|----------------------|
| Data.ts | Serialize.ts — keep internals, adopt Property.Primitive/Class/Multi API |
| Metadata.ts | Serialize.ts — used as-is |
| Event.ts | Event.ts — keep as-is, add NetworkEvent on top |
| Util.ts | core/util — isPrimitive, isConstructor, applyMixins, getMethodLabels |
| Algorithms.ts | core/util — BiMap and topologicalSort, keep as-is |
| Types.ts | core/util — UnionToIntersection, Constructor, EnumMap, keep as-is |

## Actor model
| File | Read when implementing |
|------|----------------------|
| Model.ts | Actor.ts — Axiom/assume/suppose became Aspect/get/suppose |
| ActorTrait.ts | Actor.ts — symbol-keyed traits are the conceptual ancestor |
| Sheet.ts | Actor.ts — the Sheet model is what Actor replaces |

## Server
| File | Read when implementing |
|------|----------------------|
| Websocket.ts | WsRouter — wire format { command, data } and handler pattern |
| Database.ts | Repositories — Schema<V> pattern being replaced by SQLite repos |
| SheetsEndpoint.ts | HTTP routes — what endpoints need to exist |
| Server.ts | Server.ts — startup sequence shape, swap Elysia for Hono |
| Cli.ts | Cli.ts — keep as-is |
| ExitCommand.ts | CLI commands — keep as-is |

## Content
| File | Read when implementing |
|------|----------------------|
| Barbarian.ts | Content files — @Register + @Subscribe pattern, primary example |
| Elf.ts | Content files — minimal race implementation |
| Option.ts | content/base/Option.ts — keep with minor updates |
| Stats.ts | server/data/Stats.ts — keep as-is |

## Frontend
| File | Read when implementing |
|------|----------------------|
| WebSocket.ts | WebSocket.ts — keep as-is, expand EventMap |
| ClientUtil.ts | lib/Util.ts — formatModifier, keep as-is |
| TabBar/Button/Container/Panel.svelte | Tab components — keep as-is |
| ViewCollection.svelte | View renderer — keep dynamic lookup pattern, update type names |
| AttributeList.svelte | View components — styling and ws.send pattern reference |
| AttributeTable.svelte | View components — styling reference |
| Chat.svelte | Chat.svelte — keep as-is |

## Deferred
| File | Notes |
|------|-------|
| Modifier.ts | Stat computation pipeline — revisit at M2+ |
