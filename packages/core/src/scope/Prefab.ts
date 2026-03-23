export interface PrefabActorDef {
  /** Template name — looked up at stamp time to hydrate aspects. */
  template?: string
  /**
   * Named groups of local actor IDs representing links to other actors in this prefab.
   * e.g. `{ followers: ['f1', 'f2', 'f3'] }`
   * At stamp time these local IDs are resolved to absolute UUIDs.
   */
  links?: Record<string, string[]>
}

export interface PrefabDef {
  actors: Record<string, PrefabActorDef>
}

export class Prefab {
  constructor(readonly def: PrefabDef) {}
}

export function definePrefab(def: PrefabDef): Prefab {
  return new Prefab(def)
}
