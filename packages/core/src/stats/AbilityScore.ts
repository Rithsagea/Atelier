import { Holder } from "../composite/Aspects";
import { AspectKey, Composite } from "../composite/Composite";
import { enumMap } from "../util/Types";
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

export interface AbilityScore {
  scores: Record<Ability, number>;
  refresh(sheet: Composite): void;
}

export const AbilityScore = new AspectKey<AbilityScore>("AbilityScore");

// Computed projection over the sheet's AbilityScoreContributors. Transient: holds no
// persisted state, regenerated on load and recomputed via refresh (see CORE.md).
export class ComputedAbilityScore extends Composite implements AbilityScore {
  scores: Record<Ability, number> = enumMap(Abilities, (_) => 0);

  constructor() {
    super();
    this.provide(SkillContributor, {
      source: this,
      apply: (skill: SkillScore) => {
        for (const ability of Abilities) {
          const mod = abilityModifier(this.scores[ability]);
          for (const s of AbilitySkills[ability]) skill.scores[s] += mod;
        }
      },
    });
  }

  refresh(sheet: Composite): void {
    for (const child of sheet.get(Holder).children(sheet)) {
      if (child.has(AbilityScoreContributor)) {
        child.get(AbilityScoreContributor).apply(this);
      }
    }
  }
}

export interface AbilityScoreContributor {
  readonly source: Composite;
  apply(abilityScore: AbilityScore): void;
}

export const AbilityScoreContributor = new AspectKey<AbilityScoreContributor>(
  "AbilityScoreContributor",
);
