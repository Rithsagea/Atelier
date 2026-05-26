import { Aspect, AspectKey, Composite } from "../composite/Composite";
import { enumMap } from "../util/Types";

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

@Aspect("AbilityScore")
export class AbilityScore {
  scores: Record<Ability, number> = enumMap(Abilities, (_) => 0);

  refresh(sheet: Composite): void {
    for (const v of sheet.aspects.values()) {
      if (v instanceof Composite && v.has(AbilityScoreContributor)) {
        v.get(AbilityScoreContributor).apply(this);
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
