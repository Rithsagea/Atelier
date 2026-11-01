import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { AttackContributor } from "@atelier/game/stats/Attacks";
import { Level } from "@atelier/game/stats/Level";
import { FighterSubclass } from "./FighterSubclass";

// Improved Critical: attacks critical on 19-20, from level 3.
// TODO: Remarkable Athlete, once there is initiative.
@Aspect.Variant(FighterSubclass, "Champion")
export class Champion extends Composite {
  constructor() {
    super();
    this.provide(AttackContributor, {
      apply: (attacks, owner) => {
        if (owner.get(Level).value < 3) return;
        for (const attack of attacks.list) attack.critical = Math.min(attack.critical, 19);
      },
    });
  }
}
