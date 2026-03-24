import { Actor } from "../actor/Actor";
import { ActorRef } from "../actor/ActorRef";
import type { Prefab } from "./Prefab";

export class Scope {
  private readonly actors = new Map<string, Actor>();

  add(actor: Actor): void {
    this.actors.set(actor.id, actor);
  }

  /** Throws if the actor is not in this scope. */
  get(id: string): Actor {
    const actor = this.actors.get(id);
    if (!actor) throw new Error(`Scope does not contain actor "${id}"`);
    return actor;
  }

  suppose(id: string): Actor | undefined {
    return this.actors.get(id);
  }

  all(): Actor[] {
    return [...this.actors.values()];
  }

  allWhere(predicate: (actor: Actor) => boolean): Actor[] {
    return this.all().filter(predicate);
  }

  /**
   * Stamps a prefab into a new Scope.
   * - Generates absolute UUIDs for each local actor ID.
   * - Resolves all links to ActorRef arrays with absolute IDs.
   * - Returns the populated scope and a map of localId → actor for caller convenience.
   */
  static fromPrefab(prefab: Prefab): StampResult {
    const scope = new Scope();
    const idMap = new Map<string, string>();

    // First pass: assign UUIDs
    for (const localId of Object.keys(prefab.def.actors)) {
      idMap.set(localId, crypto.randomUUID());
    }

    // Second pass: create actors and resolve links
    const links: Record<string, Record<string, ActorRef[]>> = {};
    for (const [localId, def] of Object.entries(prefab.def.actors)) {
      const uuid = idMap.get(localId)!;
      const actor = new Actor(uuid);
      scope.add(actor);

      if (def.links) {
        links[uuid] = {};
        for (const [name, refs] of Object.entries(def.links)) {
          links[uuid][name] = refs.map((ref) => {
            const resolved = idMap.get(ref);
            if (!resolved) throw new Error(`Prefab: unknown local actor ref "${ref}"`);
            return new ActorRef(resolved);
          });
        }
      }
    }

    const byLocalId = Object.fromEntries(
      [...idMap.entries()].map(([localId, uuid]) => [localId, scope.get(uuid)]),
    );

    return { scope, links, byLocalId };
  }
}

export interface StampResult {
  scope: Scope;
  /** Resolved links per actor UUID: actorId → { linkName → ActorRef[] } */
  links: Record<string, Record<string, ActorRef[]>>;
  /** Convenience map: localPrefabId → Actor */
  byLocalId: Record<string, Actor>;
}
