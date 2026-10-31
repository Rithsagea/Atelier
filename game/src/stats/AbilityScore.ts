import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { enumMap } from "@atelier/core/util/Types";
import { InitiativeContributor, type Initiative } from "./Initiative";
import { AbilitySkills, SkillContributor, type SkillScore } from "./Stats";

export const Abilities = [
  "strength",
  "dexterity",
  "constitution",
  "intelligence",
  "wisdom",
  "charisma",
] as const;
export type Ability = (typeof Abilities)[number];

export const AbilityLabels: Record<Ability, string> = {
  strength: "Strength",
  dexterity: "Dexterity",
  constitution: "Constitution",
  intelligence: "Intelligence",
  wisdom: "Wisdom",
  charisma: "Charisma",
};

export function abilityModifier(score: number): number {
  return Math.floor((score - 10) / 2);
}

// Computed projection over the sheet's AbilityScoreContributors. Transient: it declares no
// @Property fields, so it is never serialized and is rebuilt on load (see CORE.md).
@Aspect.Final("AbilityScore")
export class AbilityScore extends Composite {
  scores: Record<Ability, number> = enumMap(Abilities, (_) => 0);

  constructor() {
    super();
    this.provide(SkillContributor, {
      apply: (skill: SkillScore) => {
        for (const ability of Abilities) {
          for (const s of AbilitySkills[ability])
            skill.scores[s] += abilityModifier(this.scores[ability]);
        }
      },
    });
    this.provide(InitiativeContributor, {
      apply: (initiative: Initiative) => {
        initiative.value += abilityModifier(this.scores.dexterity);
      },
    });
  }

  refresh(sheet: Composite): void {
    contribute(AbilityScoreContributor, sheet, this);
  }
}

export const AbilityScoreContributor = ContributorKey<AbilityScore>("AbilityScoreContributor");
