import { AspectKey, AspectRef, Composite } from "./Composite";

type Entry<T extends object> = [AspectRef<T>, () => T] | [AspectRef<T>];

export function Structure<const Types extends readonly object[]>(
  ...entries: { [K in keyof Types]: Entry<Types[K]> }
) {
  const expected = entries.map(([ref]) => ref) as AspectRef<object>[];
  return class extends Composite {
    constructor() {
      super();
      for (const entry of entries) {
        if (entry.length === 2) this.provide(entry[0], entry[1]());
      }
    }
    validate(): boolean {
      return expected.every((ref) => this.has(ref));
    }
  };
}

export interface Holder<T extends Composite = Composite> {
  children(parent: T): Iterable<Composite>;
}

export const Holder = new AspectKey<Holder<Composite>>("Holder");
