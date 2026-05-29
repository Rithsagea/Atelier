import { AspectKey, AspectRef, Composite, getAspectKey } from "./Composite";
import { Property, getStrategy, type SerializationStrategy } from "../serial/Data";

type Entry<T extends object> = [AspectRef<T>, () => T] | [AspectRef<T>];

export function Structure<const Types extends readonly object[]>(
  ...entries: { [K in keyof Types]: Entry<Types[K]> }
) {
  const aspectNames = new Map<string, AspectRef<object>>();
  for (const [aspect] of entries) {
    const name = getAspectKey(aspect).name;
    if (aspectNames.has(name)) {
      throw new Error(`Structure has duplicate aspect name "${name}"`);
    }
    aspectNames.set(name, aspect);
  }

  const Klass = class extends Composite {
    constructor() {
      super();
      for (const [aspect, factory] of entries) {
        if (factory) this.provide(aspect, factory());
      }
    }
    validate(): boolean {
      for (const ref of aspectNames.values()) {
        if (!this.has(ref)) return false;
      }
      return true;
    }
  };

  Property.Serialize(AspectStrategy(entries, aspectNames))(Klass.prototype, "aspects");
  return Klass;
}

function AspectStrategy(
  entries: readonly Entry<object>[],
  refsByName: Map<string, AspectRef<object>>,
): SerializationStrategy<Map<AspectKey<object>, unknown>> {
  const strategyFor = (key: AspectKey<object>) => {
    const ctx = key.typeMap ?? key.ctor;
    return ctx ? getStrategy(ctx) : undefined;
  };
  return {
    serialize(source) {
      const out = new Map<AspectKey<object>, unknown>();
      for (const ref of refsByName.values()) {
        const key = getAspectKey(ref);
        if (!source.has(key)) continue;
        const strategy = strategyFor(key);
        if (strategy) out.set(key, strategy.serialize(source.get(key)));
      }
      return out.size === 0 ? undefined : out;
    },
    deserialize(source) {
      const map = new Map<AspectKey<object>, unknown>();
      for (const [aspect, factory] of entries) {
        if (factory && !strategyFor(getAspectKey(aspect))) {
          map.set(getAspectKey(aspect), factory());
        }
      }
      for (const [key, raw] of source as Map<AspectKey<object>, unknown>) {
        if (!refsByName.has(key.name)) throw new Error(`Unknown aspect "${key.name}"`);
        const strategy = strategyFor(key);
        if (!strategy) throw new Error(`Aspect "${key.name}" has no impl registered`);
        map.set(key, strategy.deserialize(raw));
      }
      return map;
    },
  };
}
