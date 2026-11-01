import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { enumMap } from "@atelier/core/util/Types";
import { InitiativeContributor } from "./Initiative";
import { SavingThrowContributor } from "./SavingThrow";
import { SkillContributor } from "./SkillScore";
import { type Ability, Abilities, AbilitySkills } from "./Stats";

export function abilityModifier(score: number): number {
  return Math.floor((score - 10) / 2);
}

@Aspect.Final("AbilityScore")
export class AbilityScore extends Composite {
  scores: Record<Ability, number> = enumMap(Abilities, (_) => 0);

  constructor() {
    super();
    this.provide(SkillContributor, {
      apply: (skill) => {
        for (const ability of Abilities) {
          for (const s of AbilitySkills[ability])
            skill.scores[s] += abilityModifier(this.scores[ability]);
        }
      },
    });
    this.provide(SavingThrowContributor, {
      apply: (save) => {
        for (const ability of Abilities) {
          save.scores[ability] += abilityModifier(this.scores[ability]);
        }
      },
    });
    this.provide(InitiativeContributor, {
      apply: (initiative) => {
        initiative.value += abilityModifier(this.scores.dexterity);
      },
    });
  }

  refresh(sheet: Composite): void {
    contribute(AbilityScoreContributor, sheet, this);
  }
}

export const AbilityScoreContributor = ContributorKey<AbilityScore>("AbilityScoreContributor");
