import { Holder } from "@atelier/core/composite/Aspects";
import { Aspect, AspectKey, Composite } from "@atelier/core/composite/Composite";

// Computed projection over the sheet's InitiativeContributors. Transient, like SkillScore.
@Aspect("Initiative")
export class Initiative extends Composite {
  value = 0;

  refresh(sheet: Composite): void {
    for (const child of sheet.get(Holder).children(sheet)) {
      if (child.has(InitiativeContributor)) {
        child.get(InitiativeContributor).apply(this, sheet);
      }
    }
  }
}

export interface InitiativeContributor {
  readonly source: Composite;
  apply(initiative: Initiative, owner: Composite): void;
}

export const InitiativeContributor = new AspectKey<InitiativeContributor>("InitiativeContributor");
