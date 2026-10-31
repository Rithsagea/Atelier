import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import {
  type SkillProficiency,
  SkillProficiencyContributor,
} from "@atelier/game/stats/SkillProficiency";
import { type Skill } from "@atelier/game/stats/Stats";

// Proficiency in one skill of choice. Empty until a skill is chosen.
@Aspect("Skillful")
export class Skillful extends Composite {
  @Property.Primitive
  skill?: Skill;

  constructor() {
    super();
    this.provide(SkillProficiencyContributor, {
      apply: (proficiency: SkillProficiency) => {
        if (this.skill) proficiency.skills.add(this.skill);
      },
    });
  }
}
