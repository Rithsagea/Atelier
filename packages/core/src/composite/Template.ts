import type { Aspect } from "./Aspect";
import { Composite } from "./Composite";

export class Template {
  constructor(
    readonly name: string,
    readonly aspects: Aspect<unknown>[],
  ) {}

  /** Throws listing all missing aspects if the composite doesn't satisfy this template. */
  validate(composite: Composite): void {
    const missing = this.aspects.filter((a) => !composite.has(a));
    if (missing.length > 0) {
      throw new Error(
        `Composite "${composite.id}" is missing aspects for template "${this.name}": ` +
          missing.map((a) => a.name).join(", "),
      );
    }
  }
}
