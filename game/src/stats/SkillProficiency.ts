import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { Level } from "./Level";
import { type Skill, type SkillScore, SkillContributor } from "./Stats";

// Computed projection over the sheet's SkillProficiencyContributors: which skills the
// character is proficient in. Transient, like SkillScore. A set, so a skill granted twice is
// still proficient once. Feeds SkillScore as a contributor that adds the bonus per skill.
@Aspect("SkillProficiency")
export class SkillProficiency extends Composite {
  skills = new Set<Skill>();

  constructor() {
    super();
    this.provide(SkillContributor, {
      apply: (score: SkillScore, owner: Composite) => {
        for (const skill of this.skills) {
          score.scores[skill] += owner.get(Level).proficiencyBonus;
        }
      },
    });
  }

  refresh(sheet: Composite): void {
    contribute(SkillProficiencyContributor, sheet, this);
  }
}

export const SkillProficiencyContributor = ContributorKey<SkillProficiency>(
  "SkillProficiencyContributor",
);
