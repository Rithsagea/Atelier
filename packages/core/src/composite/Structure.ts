import { AspectKey, AspectRef, Composite, getAspectKey } from "./Composite";
import {
  serialize as serializeFields,
  deserialize as deserializeFields,
  type SerializedObject,
} from "../serial/Data";
import { type Constructor } from "../util/Types";

type Entry<T extends object> = [AspectRef<T>, () => T] | [AspectRef<T>];

export function Structure<const Types extends readonly object[]>(
  ...entries: { [K in keyof Types]: Entry<Types[K]> }
) {
  const refs = entries.map(([ref]) => ref) as AspectRef<object>[];
  return class extends Composite {
    constructor() {
      super();
      for (const entry of entries) {
        if (entry.length === 2) this.provide(entry[0], entry[1]());
      }
    }

    validate(): boolean {
      return refs.every((ref) => this.has(ref));
    }

    serialize(): SerializedObject {
      const out: SerializedObject = {};
      for (const ref of refs) {
        const key = getAspectKey(ref);
        if (key.typeMap.size === 0) continue;
        if (!this.has(key)) continue;
        const value = this.get(key) as object;
        const tag = key.typeMap.getKey(value.constructor as Constructor);
        if (!tag) {
          throw new Error(
            `Aspect "${key.name}" value of type ${value.constructor.name} ` +
              `has no entry in its typeMap — register with @Aspect(...)`,
          );
        }
        out[key.name] = { $type: tag, ...serializeFields(value) };
      }
      return out;
    }

    deserialize(data: SerializedObject): void {
      for (const ref of refs) {
        const key = getAspectKey(ref);
        if (key.typeMap.size === 0) continue;
        const raw = data[key.name] as SerializedObject | undefined;
        if (!raw) continue;
        const tag = raw.$type as string;
        const ctor = key.typeMap.get(tag);
        if (!ctor) {
          throw new Error(`Unknown $type "${tag}" for aspect "${key.name}"`);
        }
        this.provide(key, deserializeFields(raw, ctor));
      }
    }
  };
}

export interface Holder<T extends Composite = Composite> {
  children(parent: T): Iterable<Composite>;
}

export const Holder = new AspectKey<Holder<Composite>>("Holder");
