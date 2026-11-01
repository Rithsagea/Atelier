import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";

export type ArmorCategory = "light" | "medium" | "heavy" | "shield";

// Computed projection over the sheet's ArmorTrainingContributors: which armor the character is
// trained with. Transient, like SkillProficiency. Only a Shield checks it (no training, no AC);
// armor worn untrained still counts for AC.
@Aspect.Final("ArmorTraining")
export class ArmorTraining extends Composite {
  categories = new Set<ArmorCategory>();

  refresh(sheet: Composite): void {
    contribute(ArmorTrainingContributor, sheet, this);
  }
}

export const ArmorTrainingContributor = ContributorKey<ArmorTraining>("ArmorTrainingContributor");
