import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { enumMap } from "@atelier/core/util/Types";
import { Skills, type Skill } from "./Stats";

// Computed projection over the sheet's SkillContributors. Transient: it declares no
// @Property fields, so it is never serialized and is rebuilt on load (see CORE.md).
@Aspect.Final("SkillScore")
export class SkillScore extends Composite {
  scores: Record<Skill, number> = enumMap(Skills, (_) => 0);

  refresh(sheet: Composite): void {
    contribute(SkillContributor, sheet, this);
  }
}

export const SkillContributor = ContributorKey<SkillScore>("SkillContributor");
