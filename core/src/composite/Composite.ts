import { Constructor } from "../util/Types";
import { BiMap } from "../util/Algorithms";

// Aspects

/** Identifies an aspect. A plain key is never serialized (capabilities, computed stats). */
export class AspectKey<T extends object> {
  declare readonly _type: T;

  constructor(readonly name: string) {}
}

/** The key of a final aspect: exactly one class, serialized as that class. */
export class FinalKey<T extends object> extends AspectKey<T> {
  constructor(
    name: string,
    readonly ctor: Constructor<T>,
  ) {
    super(name);
  }
}

/** The key of a polymorphic slot: many variants, serialized with a `$type` tag. */
export class RegistryKey<T extends object> extends AspectKey<T> {
  readonly typeMap = new BiMap<string, Constructor<T>>();
}

const finalKeys = new WeakMap<Constructor, FinalKey<object>>();

export const Aspect = {
  /** Declares a class as an aspect that has no variants; the class is its own key. */
  Final(id: string) {
    return (ctor: Constructor) => {
      finalKeys.set(ctor, new FinalKey(id, ctor));
    };
  },

  /** Registers a class as one variant of a polymorphic slot. */
  Variant<T extends object>(key: RegistryKey<T>, id: string) {
    return (ctor: Constructor<T>) => {
      if (!key.typeMap.set(id, ctor)) {
        throw new Error(
          `Aspect variant "${id}" already registered under "${key.name}" ` +
            `(or constructor ${ctor.name} already mapped under that key)`,
        );
      }
    };
  },
};

export type AspectRef<T extends object> = AspectKey<T> | Constructor<T>;

export function getAspectKey<T extends object>(ref: AspectRef<T>) {
  if (ref instanceof AspectKey) return ref;
  const key = finalKeys.get(ref);
  if (!key) throw new Error(`${ref.name} is not registered as an aspect - add @Aspect.Final(...)`);
  return key as unknown as AspectKey<T>;
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
