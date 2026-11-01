import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { Level } from "./Level";
import { SkillContributor } from "./SkillScore";
import { type Skill } from "./Stats";

// Which skills the character is proficient in. A set, so a skill granted twice counts once.
@Aspect.Final("SkillProficiency")
export class SkillProficiency extends Composite {
  skills = new Set<Skill>();

  constructor() {
    super();
    this.provide(SkillContributor, {
      apply: (score, owner) => {
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
