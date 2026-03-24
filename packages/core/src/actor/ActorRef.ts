import { Property, type SerializationStrategy } from "../serial/Data";

/**
 * Typed wrapper for inter-actor references. Never use raw strings.
 *
 * At prefab definition time, ids are local strings ('leader', 'f1').
 * At stamp time, Scope.fromPrefab rewrites all ActorRef ids to absolute UUIDs.
 */
export class ActorRef {
  @Property.Primitive
  id: string;

  constructor(id: string) {
    this.id = id;
  }
}

/** SerializationStrategy for use in @Property.Map and @Property.List */
export const ActorRefContext: SerializationStrategy<ActorRef> = {
  serialize: (ref: ActorRef) => ref.id,
  deserialize: (raw: string) => new ActorRef(raw),
};
