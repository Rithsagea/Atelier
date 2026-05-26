import type { Aspect } from "./Aspect";

export class Composite {
  readonly id: string;

  // TRANSIENT — aspects are not serialized on Composite itself;
  // serialization is handled by the DB layer (id + template + JSON blob per aspect)
  private readonly aspects = new Map<symbol, unknown>();

  constructor(id: string = crypto.randomUUID()) {
    this.id = id;
  }

  provide<T>(aspect: Aspect<T>, value: T): void {
    this.aspects.set(aspect.id, value);
  }

  /** Throws if the aspect is not present. Missing aspect = content bug. */
  get<T>(aspect: Aspect<T>): T {
    if (!this.aspects.has(aspect.id))
      throw new Error(`Composite "${this.id}" does not have aspect "${aspect.name}"`);
    return this.aspects.get(aspect.id) as T;
  }

  suppose<T>(aspect: Aspect<T>): T | undefined {
    return this.aspects.get(aspect.id) as T | undefined;
  }

  has(aspect: Aspect<unknown>): boolean {
    return this.aspects.has(aspect.id);
  }
}
