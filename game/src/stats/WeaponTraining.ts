import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";

export type WeaponCategory = "simple" | "martial";

// Computed projection over the sheet's WeaponTrainingContributors: which weapon categories the
// character is proficient with. Transient, like ArmorTraining. An untrained weapon still attacks,
// just without the proficiency bonus.
@Aspect.Final("WeaponTraining")
export class WeaponTraining extends Composite {
  categories = new Set<WeaponCategory>();

  refresh(sheet: Composite): void {
    contribute(WeaponTrainingContributor, sheet, this);
  }
}

export const WeaponTrainingContributor = ContributorKey<WeaponTraining>(
  "WeaponTrainingContributor",
);
