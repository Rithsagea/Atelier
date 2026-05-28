import { Constructor } from "../util/Types";
import { BiMap } from "../util/Algorithms";
import {
  Property,
  serialize as serializeFields,
  deserialize as deserializeFields,
  type SerializedObject,
  type SerializationStrategy,
} from "../serial/Data";

// Aspects

const aspectKeyByName = new Map<string, AspectKey<unknown>>();
const aspectKeyById = new Map<symbol, AspectKey<unknown>>();

export class AspectKey<T> {
  declare readonly _type: T;
  readonly id: symbol;
  readonly name: string;
  readonly typeMap = new BiMap<string, Constructor<T>>();

  constructor(name: string) {
    if (aspectKeyByName.has(name)) {
      throw new Error(`AspectKey "${name}" already registered`);
    }
    this.name = name;
    this.id = Symbol(name);
    aspectKeyByName.set(name, this as AspectKey<unknown>);
    aspectKeyById.set(this.id, this as AspectKey<unknown>);
  }
}

const aspectRegistry = new WeakMap<Constructor, AspectKey<unknown>>();

export function Aspect<T extends object>(id: string, key?: AspectKey<T>) {
  return (ctor: Constructor) => {
    if (key) {
      if (!key.typeMap.set(id, ctor as Constructor<T>)) {
        throw new Error(
          `Aspect impl "${id}" already registered under "${key.name}" ` +
            `(or constructor ${ctor.name} already mapped under that key)`,
        );
      }
    } else {
      const newKey = new AspectKey(id);
      newKey.typeMap.set(id, ctor);
      aspectRegistry.set(ctor, newKey);
    }
  };
}

export type AspectRef<T extends object> = AspectKey<T> | Constructor<T>;

export function getAspectKey<T extends object>(ref: AspectRef<T>) {
  if (ref instanceof AspectKey) return ref;
  const key = aspectRegistry.get(ref);
  if (!key) throw new Error(`${ref.name} is not registered as an aspect - add @Aspect(...)`);
  return key as AspectKey<T>;
}

// Aspect-map strategy: encodes Composite.aspects as { [name]: { $type, ...fields } }

const aspectsStrategy: SerializationStrategy<Map<symbol, unknown>> = {
  serialize(source) {
    const out: SerializedObject = {};
    for (const [symId, value] of source) {
      const key = aspectKeyById.get(symId);
      if (!key || key.typeMap.size === 0) continue;
      const v = value as object;
      const tag = key.typeMap.getKey(v.constructor as Constructor);
      if (!tag) {
        throw new Error(
          `Aspect "${key.name}" value of type ${v.constructor.name} ` +
            `has no entry in its typeMap`,
        );
      }
      out[key.name] = { $type: tag, ...serializeFields(v) };
    }
    return Object.keys(out).length === 0 ? undefined : out;
  },
  deserialize(source, current) {
    const map = current ?? new Map<symbol, unknown>();
    for (const [name, raw] of Object.entries(source as SerializedObject)) {
      const key = aspectKeyByName.get(name);
      if (!key) throw new Error(`Unknown aspect "${name}"`);
      const r = raw as SerializedObject;
      const ctor = key.typeMap.get(r.$type as string);
      if (!ctor) {
        throw new Error(`Unknown $type "${r.$type}" for aspect "${name}"`);
      }
      map.set(key.id, deserializeFields(r, ctor));
    }
    return map;
  },
};

// Composite

export class Composite {
  @Property.Serialize(aspectsStrategy)
  readonly aspects = new Map<symbol, unknown>();

  provide<T extends object>(aspect: AspectRef<T>, value: T): void {
    this.aspects.set(getAspectKey(aspect).id, value);
  }

  /** Throws if the aspect is not present. Missing aspect = content bug. */
  get<T extends object>(ref: AspectRef<T>): T {
    const key = getAspectKey(ref);
    if (!this.aspects.has(key.id)) throw new Error(`Composite does not have aspect "${key.name}"`);
    return this.aspects.get(key.id) as T;
  }

  suppose<T extends object>(aspect: AspectRef<T>): T | undefined {
    return this.aspects.get(getAspectKey(aspect).id) as T | undefined;
  }

  has<T extends object>(ref: AspectRef<T>): boolean {
    return this.aspects.has(getAspectKey(ref).id);
  }
}
