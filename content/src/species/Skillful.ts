import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import { SkillProficiencyContributor } from "@atelier/game/stats/SkillProficiency";
import { type Skill } from "@atelier/game/stats/Stats";

// Proficiency in one skill of choice. Empty until a skill is chosen.
@Aspect.Final("Skillful")
export class Skillful extends Composite {
  @Property.Primitive
  skill?: Skill;

  constructor() {
    super();
    this.provide(SkillProficiencyContributor, {
      apply: (proficiency) => {
        if (this.skill) proficiency.skills.add(this.skill);
      },
    });
  }
}
