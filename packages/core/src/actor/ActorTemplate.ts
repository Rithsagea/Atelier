import type { Aspect } from './Aspect'
import { Actor } from './Actor'

export class ActorTemplate {
  constructor(
    readonly name: string,
    readonly aspects: Aspect<unknown>[],
  ) {}

  /** Throws listing all missing aspects if the actor doesn't satisfy this template. */
  validate(actor: Actor): void {
    const missing = this.aspects.filter((a) => !actor.has(a))
    if (missing.length > 0) {
      throw new Error(
        `Actor "${actor.id}" is missing aspects for template "${this.name}": ` +
          missing.map((a) => a.name).join(', '),
      )
    }
  }

  /** Creates an empty Actor without any aspects attached. */
  create(): Actor {
    return new Actor()
  }
}
