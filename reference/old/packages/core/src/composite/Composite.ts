import { Constructor } from "../util/Types";
import { BiMap } from "../util/Algorithms";

// Aspects

export class AspectKey<T extends object> {
  declare readonly _type: T;
  readonly name: string;
  ctor?: Constructor<T>;
  typeMap?: BiMap<string, Constructor<T>>;

  constructor(name: string) {
    this.name = name;
  }
}

const aspectRegistry = new WeakMap<Constructor, AspectKey<object>>();

export function Aspect<T extends object>(id: string, key?: AspectKey<T>) {
  return (ctor: Constructor) => {
    if (key) {
      key.typeMap ??= new BiMap<string, Constructor<T>>();
      if (!key.typeMap.set(id, ctor as Constructor<T>)) {
        throw new Error(
          `Aspect impl "${id}" already registered under "${key.name}" ` +
            `(or constructor ${ctor.name} already mapped under that key)`,
        );
      }
    } else {
      const newKey = new AspectKey(id);
      newKey.ctor = ctor;
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

// Composite

export class Composite {
  readonly aspects = new Map<AspectKey<object>, unknown>();

  provide<T extends object>(aspect: AspectRef<T>, value: T): void {
    this.aspects.set(getAspectKey(aspect), value);
  }

  /** Throws if the aspect is not present. Missing aspect = content bug. */
  get<T extends object>(aspect: AspectRef<T>): T {
    const key = getAspectKey(aspect);
    if (!this.aspects.has(key)) throw new Error(`Composite does not have aspect "${key.name}"`);
    return this.aspects.get(key) as T;
  }

  suppose<T extends object>(aspect: AspectRef<T>): T | undefined {
    return this.aspects.get(getAspectKey(aspect)) as T | undefined;
  }

  has<T extends object>(aspect: AspectRef<T>): boolean {
    return this.aspects.has(getAspectKey(aspect));
  }
}
