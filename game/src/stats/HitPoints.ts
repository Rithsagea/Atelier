import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { Property } from "@atelier/core/serial/Data";

// Computed projection over the sheet's MaxHitPointsContributors. Transient, like Initiative.
@Aspect.Final("MaxHitPoints")
export class MaxHitPoints extends Composite {
  value = 0;

  refresh(sheet: Composite): void {
    contribute(MaxHitPointsContributor, sheet, this);
  }
}

export const MaxHitPointsContributor = ContributorKey<MaxHitPoints>("MaxHitPointsContributor");

// Stores what was lost, not what is left, so a change to the maximum (Constitution, level, a
// feat) needs no fix-up: current is always max minus damage.
@Aspect.Final("HitPoints")
export class HitPoints {
  @Property.Primitive
  damage: number = 0;

  @Property.Primitive
  temporary: number = 0;

  current(sheet: Composite): number {
    return sheet.get(MaxHitPoints).value - this.damage;
  }

  /** Temporary Hit Points go first; the rest comes off Hit Points, down to 0. */
  take(sheet: Composite, amount: number): void {
    const absorbed = Math.min(this.temporary, amount);
    this.temporary -= absorbed;
    this.damage = Math.min(sheet.get(MaxHitPoints).value, this.damage + amount - absorbed);
  }

  /** Healing restores Hit Points up to the maximum; it never touches temporary ones. */
  heal(amount: number): void {
    this.damage = Math.max(0, this.damage - amount);
  }

  /** Temporary Hit Points don't stack: keep whichever is larger. */
  grantTemporary(amount: number): void {
    this.temporary = Math.max(this.temporary, amount);
  }
}
