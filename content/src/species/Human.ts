import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import { Level } from "@atelier/game/stats/Level";
import { type Skill, type SkillScore, SkillContributor } from "@atelier/game/stats/Stats";
import { Species } from "@atelier/game/species/Species";

// Skillful: proficiency in one skill of choice. Versatile (an Origin feat of choice) is
// not modelled here; the chosen feat lives in the sheet's Feats.
@Aspect("Human", Species)
export class Human extends Composite {
  @Property.Primitive
  readonly skill: Skill;

  constructor(skill: Skill = "athletics") {
    super();
    this.skill = skill;
    this.provide(SkillContributor, {
      apply: (skills: SkillScore, owner: Composite) => {
        skills.scores[this.skill] += owner.get(Level).proficiencyBonus;
      },
    });
  }
}
