import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { Property } from "@atelier/core/serial/Data";

export const Dice = ["d6", "d8", "d10", "d12"] as const;
export type Die = (typeof Dice)[number];

// Computed projection over the sheet's HitDicePoolContributors: how many Hit Dice of each size
// the character has in total (the class grants one per level). Transient, like Initiative.
@Aspect.Final("HitDicePool")
export class HitDicePool extends Composite {
  dice: Partial<Record<Die, number>> = {};

  refresh(sheet: Composite): void {
    contribute(HitDicePoolContributor, sheet, this);
  }
}

export const HitDicePoolContributor = ContributorKey<HitDicePool>("HitDicePoolContributor");

// How many Hit Dice of each size have been spent. The pool they come out of is derived.
@Aspect.Final("HitDice")
export class HitDice {
  @Property.Primitive
  spent: Partial<Record<Die, number>> = {};

  remaining(sheet: Composite, die: Die): number {
    return (sheet.get(HitDicePool).dice[die] ?? 0) - (this.spent[die] ?? 0);
  }

  /** Mark one die spent. Rolling it and healing is the short rest's job. */
  spend(sheet: Composite, die: Die): void {
    if (this.remaining(sheet, die) < 1) throw new Error(`No ${die} Hit Dice left to spend`);
    this.spent[die] = (this.spent[die] ?? 0) + 1;
  }
}
