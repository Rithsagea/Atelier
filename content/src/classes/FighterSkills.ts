import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import {
  type SkillProficiency,
  SkillProficiencyContributor,
} from "@atelier/game/stats/SkillProficiency";
import { type Skill } from "@atelier/game/stats/Stats";

export const FighterSkillList = [
  "acrobatics",
  "animal_handling",
  "athletics",
  "history",
  "insight",
  "intimidation",
  "persuasion",
  "perception",
  "survival",
] as const satisfies readonly Skill[];

// Proficiency in two skills from the Fighter list. Empty until chosen.
@Aspect.Final("FighterSkills")
export class FighterSkills extends Composite {
  @Property.Primitive
  skills: Skill[] = [];

  constructor() {
    super();
    this.provide(SkillProficiencyContributor, {
      apply: (proficiency: SkillProficiency) => {
        for (const skill of this.skills) proficiency.skills.add(skill);
      },
    });
  }

  choose(first: Skill, second: Skill): void {
    if (first === second) throw new Error(`Fighter skills must be different: ${first}`);
    for (const skill of [first, second]) {
      if (!(FighterSkillList as readonly Skill[]).includes(skill)) {
        throw new Error(`${skill} is not a Fighter skill`);
      }
    }
    this.skills = [first, second];
  }
}
