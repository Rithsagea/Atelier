import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { AbilityScore, abilityModifier } from "@atelier/game/stats/AbilityScore";
import { type ArmorTraining, ArmorTrainingContributor } from "@atelier/game/stats/ArmorTraining";
import { type HitDicePool, HitDicePoolContributor } from "@atelier/game/stats/HitDice";
import { type MaxHitPoints, MaxHitPointsContributor } from "@atelier/game/stats/HitPoints";
import { Level } from "@atelier/game/stats/Level";
import {
  type SavingThrowProficiency,
  SavingThrowProficiencyContributor,
} from "@atelier/game/stats/SavingThrow";

// The Core Fighter Traits that scale with level or feed a stat. Stores nothing, so it is rebuilt
// on load. Hit Points use the fixed value (6 + Con per level after the first), not a roll.
// TODO: weapon proficiency, once there are weapons.
@Aspect.Final("FighterTraits")
export class FighterTraits extends Composite {
  constructor() {
    super();
    this.provide(MaxHitPointsContributor, {
      apply: (max: MaxHitPoints, owner: Composite) => {
        const level = owner.get(Level).value;
        const constitution = abilityModifier(owner.get(AbilityScore).scores.constitution);
        max.value += 10 + 6 * (level - 1) + constitution * level;
      },
    });
    this.provide(HitDicePoolContributor, {
      apply: (pool: HitDicePool, owner: Composite) => {
        pool.dice.d10 = (pool.dice.d10 ?? 0) + owner.get(Level).value;
      },
    });
    this.provide(SavingThrowProficiencyContributor, {
      apply: (proficiency: SavingThrowProficiency) => {
        proficiency.abilities.add("strength").add("constitution");
      },
    });
    this.provide(ArmorTrainingContributor, {
      apply: (training: ArmorTraining) => {
        for (const category of ["light", "medium", "heavy", "shield"] as const) {
          training.categories.add(category);
        }
      },
    });
  }
}
