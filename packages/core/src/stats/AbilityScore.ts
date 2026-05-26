import { Aspect } from "../composite/Aspect";
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

export class AbilityScore {
  scores: Record<Ability, number> = enumMap(Abilities, (_) => 0);
}

export const AbilityScoreAspect = new Aspect<AbilityScore>("AbilityScores");

export interface AbilityScoreMutator {}

export const AbilityScoreMutatorAspect = new Aspect<AbilityScoreMutator>("AbilityScoreMutator");
