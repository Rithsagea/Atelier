import { Holder } from "../composite/Aspects";
import { Aspect, AspectKey, Composite } from "../composite/Composite";
import { enumMap } from "../util/Types";
import { type Ability } from "./AbilityScore";

export const Skills = [
  "acrobatics",
  "animal_handling",
  "arcana",
  "athletics",
  "deception",
  "history",
  "insight",
  "intimidation",
  "investigation",
  "medicine",
  "nature",
  "perception",
  "performance",
  "persuasion",
  "religion",
  "sleight_of_hand",
  "stealth",
  "survival",
] as const;
export type Skill = (typeof Skills)[number];

export const SkillLabels: Record<Skill, string> = {
  acrobatics: "Acrobatics",
  animal_handling: "Animal Handling",
  arcana: "Arcana",
  athletics: "Athletics",
  deception: "Deception",
  history: "History",
  insight: "Insight",
  intimidation: "Intimidation",
  investigation: "Investigation",
  medicine: "Medicine",
  nature: "Nature",
  perception: "Perception",
  performance: "Performance",
  persuasion: "Persuasion",
  religion: "Religion",
  sleight_of_hand: "Sleight of Hand",
  stealth: "Stealth",
  survival: "Survival",
};

export const AbilitySkills: Record<Ability, Skill[]> = {
  strength: ["athletics"],
  dexterity: ["acrobatics", "sleight_of_hand", "stealth"],
  constitution: [],
  intelligence: ["arcana", "history", "investigation", "nature", "religion"],
  wisdom: ["animal_handling", "insight", "medicine", "perception", "survival"],
  charisma: ["deception", "intimidation", "performance", "persuasion"],
};

@Aspect("SkillScore")
export class SkillScore extends Composite {
  scores: Record<Skill, number> = enumMap(Skills, (_) => 0);

  refresh(sheet: Composite): void {
    for (const child of sheet.get(Holder).children(sheet)) {
      if (child.has(SkillContributor)) {
        child.get(SkillContributor).apply(this);
      }
    }
  }
}

export interface SkillContributor {
  readonly source: Composite;
  apply(skill: SkillScore): void;
}

export const SkillContributor = new AspectKey<SkillContributor>("SkillContributor");
