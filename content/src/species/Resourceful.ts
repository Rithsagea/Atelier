import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { LongRestContributor } from "@atelier/game/stats/HeroicInspiration";

// Heroic Inspiration after every long rest.
@Aspect.Final("Resourceful")
export class Resourceful extends Composite {
  constructor() {
    super();
    this.provide(LongRestContributor, {
      apply: (inspiration) => {
        inspiration.value = true;
      },
    });
  }
}
