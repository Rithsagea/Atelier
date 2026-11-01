import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { type DiceRoll } from "../dice/Rng";
import { type DamageType, type Weapon } from "../equipment/Weapon";

// One way to attack, worked out for this character: roll a d20 plus `bonus` against the target's
// AC, and on a hit roll `damage` and add `damageBonus`.
export interface Attack {
  weapon: Weapon;
  ranged: boolean;
  bonus: number;
  damage: DiceRoll;
  damageBonus: number;
  damageType: DamageType;
}

// Computed projection of the character's attacks, one per equipped weapon. Transient, like
// ArmorClass. Built in two passes so content never depends on contributor order: wielded weapons
// first add their entries (the base contributors), then features adjust the entries that exist
// (the contributors: Archery, Improved Critical, a magic weapon's bonus).
@Aspect.Final("Attacks")
export class Attacks extends Composite {
  list: Attack[] = [];

  refresh(sheet: Composite): void {
    contribute(AttackBaseContributor, sheet, this);
    contribute(AttackContributor, sheet, this);
  }
}

export const AttackBaseContributor = ContributorKey<Attacks>("AttackBaseContributor");
export const AttackContributor = ContributorKey<Attacks>("AttackContributor");
