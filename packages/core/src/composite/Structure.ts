import { AspectKey, AspectRef, Composite, getAspectKey } from "./Composite";
import {
  Property,
  serialize,
  deserialize,
  type SerializedObject,
  type SerializationStrategy,
} from "../serial/Data";
import { type Constructor } from "../util/Types";

type Entry<T extends object> = [AspectRef<T>, () => T] | [AspectRef<T>];

export function Structure<const Types extends readonly object[]>(
  ...entries: { [K in keyof Types]: Entry<Types[K]> }
) {
  const refs = entries.map(([ref]) => ref) as AspectRef<object>[];
  const refsByName = new Map<string, AspectRef<object>>();
  for (const ref of refs) {
    const name = getAspectKey(ref).name;
    if (refsByName.has(name)) {
      throw new Error(`Structure has duplicate aspect name "${name}"`);
    }
    refsByName.set(name, ref);
  }

  const Klass = class extends Composite {
    constructor() {
      super();
      for (const entry of entries) {
        if (entry.length === 2) this.provide(entry[0], entry[1]());
      }
    }
    validate(): boolean {
      return refs.every((ref) => this.has(ref));
    }
  };

  Property.Serialize(makeAspectsStrategy(entries as readonly Entry<object>[], refs, refsByName))(
    Klass.prototype,
    "aspects",
  );
  return Klass;
}

function makeAspectsStrategy(
  entries: readonly Entry<object>[],
  refs: AspectRef<object>[],
  refsByName: Map<string, AspectRef<object>>,
): SerializationStrategy<Map<symbol, unknown>> {
  return {
    serialize(source) {
      const out: SerializedObject = {};
      for (const ref of refs) {
        const key = getAspectKey(ref);
        if (!source.has(key.id)) continue;
        const v = source.get(key.id) as object;
        if (key.typeMap) {
          const tag = key.typeMap.getKey(v.constructor as Constructor);
          if (!tag) {
            throw new Error(
              `Aspect "${key.name}" value of type ${v.constructor.name} ` +
                `has no entry in its typeMap`,
            );
          }
          out[key.name] = { $type: tag, ...serialize(v) };
        } else if (key.ctor) {
          out[key.name] = serialize(v);
        }
      }
      return Object.keys(out).length === 0 ? undefined : out;
    },
    deserialize(source) {
      const map = new Map<symbol, unknown>();
      for (const entry of entries) {
        if (entry.length !== 2) continue;
        const key = getAspectKey(entry[0]);
        if (!key.ctor && !key.typeMap) {
          map.set(key.id, entry[1]());
        }
      }
      for (const [name, raw] of Object.entries(source as SerializedObject)) {
        const ref = refsByName.get(name);
        if (!ref) throw new Error(`Unknown aspect "${name}"`);
        const key = getAspectKey(ref);
        const r = raw as SerializedObject;
        let ctor: Constructor | undefined;
        if (key.typeMap) {
          ctor = key.typeMap.get(r.$type as string);
          if (!ctor) {
            throw new Error(`Unknown $type "${r.$type}" for aspect "${name}"`);
          }
        } else if (key.ctor) {
          ctor = key.ctor;
        } else {
          throw new Error(`Aspect "${name}" has no impl registered`);
        }
        map.set(key.id, deserialize(r, ctor));
      }
      return map;
    },
  };
}

export interface Holder<T extends Composite = Composite> {
  children(parent: T): Iterable<Composite>;
}

export const Holder = new AspectKey<Holder<Composite>>("Holder");
