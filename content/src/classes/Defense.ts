import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { ArmorClassContributor } from "@atelier/game/stats/ArmorClass";
import { FightingStyle } from "./FightingStyle";

// +1 AC while wearing armor.
@Aspect.Variant(FightingStyle, "Defense")
export class Defense extends Composite {
  constructor() {
    super();
    this.provide(ArmorClassContributor, {
      apply: (ac) => {
        if (ac.armor) ac.bonus += 1;
      },
    });
  }
}
