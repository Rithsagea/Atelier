import { Constructor } from "../util/Types";
import { BiMap } from "../util/Algorithms";

// Aspects

export class AspectKey<T> {
  declare readonly _type: T;
  readonly id: symbol;
  readonly name: string;

  constructor(name: string) {
    this.name = name;
    this.id = Symbol(name);
  }
}

const aspectRegistry = new WeakMap<Constructor, AspectKey<unknown>>();
const aspectTypeMaps = new Map<symbol, BiMap<string, Constructor>>();

export function Aspect<T extends object>(id: string, key?: AspectKey<T>) {
  return (ctor: Constructor) => {
    if (key) {
      const map = getAspectTypeMap(key);
      if (!map.set(id, ctor as Constructor<T>)) {
        throw new Error(
          `Aspect impl "${id}" already registered under "${key.name}" ` +
            `(or constructor ${ctor.name} already mapped under that key)`,
        );
      }
    } else {
      aspectRegistry.set(ctor, new AspectKey(id));
    }
  };
}

export function getAspectTypeMap<T extends object>(
  key: AspectKey<T>,
): BiMap<string, Constructor<T>> {
  let map = aspectTypeMaps.get(key.id);
  if (!map) {
    map = new BiMap<string, Constructor>();
    aspectTypeMaps.set(key.id, map);
  }
  return map as BiMap<string, Constructor<T>>;
}

export type AspectRef<T extends object> = AspectKey<T> | Constructor<T>;

export function getAspectKey<T extends object>(ref: AspectRef<T>) {
  if (ref instanceof AspectKey) return ref;
  const key = aspectRegistry.get(ref);
  if (!key) throw new Error(`${ref.name} is not registered as an aspect - add @Aspect(...)`);
  return key as AspectKey<T>;
}

// Composite

export class Composite {
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
