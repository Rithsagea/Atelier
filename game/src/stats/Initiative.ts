import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";

@Aspect.Final("Initiative")
export class Initiative extends Composite {
  value = 0;

  refresh(sheet: Composite): void {
    contribute(InitiativeContributor, sheet, this);
  }
}

export const InitiativeContributor = ContributorKey<Initiative>("InitiativeContributor");
