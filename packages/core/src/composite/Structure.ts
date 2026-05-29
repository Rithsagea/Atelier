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

// A serialized aspect carries no persisted state when it is absent or an empty plain object.
function isEmpty(value: unknown): boolean {
  return (
    value === undefined ||
    (typeof value === "object" &&
      value !== null &&
      value.constructor === Object &&
      Object.keys(value).length === 0)
  );
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
        if (!strategy) continue;
        // An aspect with no @Property fields serializes to an empty object: it persists
        // nothing and is transient. Omit it; its factory rebuilds it on load.
        const value = strategy.serialize(source.get(key));
        if (!isEmpty(value)) out.set(key, value);
      }
      return out.size === 0 ? undefined : out;
    },
    deserialize(source) {
      const data = source as Map<AspectKey<object>, unknown>;
      const map = new Map<AspectKey<object>, unknown>();
      // Rebuild every factoried aspect that was not persisted (transients, defaults).
      for (const [aspect, factory] of entries) {
        const key = getAspectKey(aspect);
        if (factory && !data.has(key)) map.set(key, factory());
      }
      for (const [key, raw] of data) {
        if (!refsByName.has(key.name)) throw new Error(`Unknown aspect "${key.name}"`);
        const strategy = strategyFor(key);
        if (!strategy) throw new Error(`Aspect "${key.name}" has no impl registered`);
        map.set(key, strategy.deserialize(raw));
      }
      return map;
    },
  };
}
