import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { type Initiative, InitiativeContributor } from "@atelier/game/stats/Initiative";
import { Level } from "@atelier/game/stats/Level";
import { Feat } from "@atelier/game/feats/Feat";

// Initiative Proficiency only; Initiative Swap needs an encounter.
@Aspect("Alert", Feat)
export class Alert extends Composite {
  constructor() {
    super();
    this.provide(InitiativeContributor, {
      source: this,
      apply: (initiative: Initiative, owner: Composite) => {
        initiative.value += owner.get(Level).proficiencyBonus;
      },
    });
  }
}
