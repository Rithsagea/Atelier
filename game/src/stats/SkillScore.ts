import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { enumMap } from "@atelier/core/util/Types";
import { Skills, type Skill } from "./Stats";

@Aspect.Final("SkillScore")
export class SkillScore extends Composite {
  scores: Record<Skill, number> = enumMap(Skills, (_) => 0);

  refresh(sheet: Composite): void {
    contribute(SkillContributor, sheet, this);
  }
}

export const SkillContributor = ContributorKey<SkillScore>("SkillContributor");
