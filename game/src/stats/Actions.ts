import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import type { Pending } from "../game/Pending";

// An action that does not apply right now does not exist: `exists` is checked when actions are
// listed. `start` pays any cost and returns the interaction that carries the action out.
export interface Action {
  name: string;
  exists(sheet: Composite): boolean;
  start(sheet: Composite): Pending;
}

// The actions features add; the ones every creature has live in `game/Play`.
@Aspect.Final("Actions")
export class Actions extends Composite {
  list: Action[] = [];

  refresh(sheet: Composite): void {
    contribute(ActionContributor, sheet, this);
  }
}

export const ActionContributor = ContributorKey<Actions>("ActionContributor");
