import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { InitiativeContributor } from "@atelier/game/stats/Initiative";
import { Level } from "@atelier/game/stats/Level";
import { Feat } from "@atelier/game/feats/Feat";

// Initiative Proficiency only.
// TODO: Initiative Swap, once there is an encounter.
@Aspect.Variant(Feat, "Alert")
export class Alert extends Composite {
  constructor() {
    super();
    this.provide(InitiativeContributor, {
      apply: (initiative, owner) => {
        initiative.value += owner.get(Level).proficiencyBonus;
      },
    });
  }
}
