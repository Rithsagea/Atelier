import { AspectKey, AspectRef, Composite } from "./Composite";

export function Structure<const Types extends readonly object[]>(
  ...aspects: { [K in keyof Types]: [AspectRef<Types[K]>, () => Types[K]] }
) {
  return class extends Composite {
    constructor() {
      super();
      for (const [ref, provider] of aspects) this.provide(ref, provider());
    }
  };
}

interface Holder<T> {
  children(parent: T): Generator<Iterable<Composite>>;
}

export const Holder = new AspectKey<Holder<any>>("Holder");
