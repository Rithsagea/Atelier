import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { ActionContributor } from "@atelier/game/stats/Actions";
import { AbilityScore, abilityModifier } from "@atelier/game/stats/AbilityScore";
import { ArmorTrainingContributor } from "@atelier/game/stats/ArmorTraining";
import { HitDicePoolContributor } from "@atelier/game/stats/HitDice";
import { MaxHitPointsContributor } from "@atelier/game/stats/HitPoints";
import { Level } from "@atelier/game/stats/Level";
import { ResourceContributor } from "@atelier/game/stats/Resources";
import { WeaponTrainingContributor } from "@atelier/game/stats/WeaponTraining";
import { SavingThrowProficiencyContributor } from "@atelier/game/stats/SavingThrow";
import { secondWind } from "./SecondWind";

// The Core Fighter Traits that scale with level or feed a stat. Stores nothing, so it is rebuilt
// on load. Hit Points use the fixed value (6 + Con per level after the first), not a roll.
@Aspect.Final("FighterTraits")
export class FighterTraits extends Composite {
  constructor() {
    super();
    this.provide(MaxHitPointsContributor, {
      apply: (max, owner) => {
        const level = owner.get(Level).value;
        const constitution = abilityModifier(owner.get(AbilityScore).scores.constitution);
        max.value += 10 + 6 * (level - 1) + constitution * level;
      },
    });
    this.provide(HitDicePoolContributor, {
      apply: (pool, owner) => {
        pool.dice.d10 = (pool.dice.d10 ?? 0) + owner.get(Level).value;
      },
    });
    this.provide(SavingThrowProficiencyContributor, {
      apply: (proficiency) => {
        proficiency.abilities.add("strength").add("constitution");
      },
    });
    this.provide(ArmorTrainingContributor, {
      apply: (training) => {
        for (const category of ["light", "medium", "heavy", "shield"] as const) {
          training.categories.add(category);
        }
      },
    });
    this.provide(ResourceContributor, {
      apply: (pool) => {
        pool.pools["Second Wind"] = { max: 2, onShortRest: 1 };
      },
    });
    this.provide(ActionContributor, {
      apply: (actions) => {
        actions.list.push(secondWind);
      },
    });
    this.provide(WeaponTrainingContributor, {
      apply: (training) => {
        training.categories.add("simple").add("martial");
      },
    });
  }
}
