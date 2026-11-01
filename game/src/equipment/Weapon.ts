import { type DiceRoll } from "../dice/Rng";
import { AbilityScore, abilityModifier } from "../stats/AbilityScore";
import { AttackBaseContributor } from "../stats/Attacks";
import { Level } from "../stats/Level";
import { WeaponTraining, type WeaponCategory } from "../stats/WeaponTraining";
import { Equippable } from "./Item";

export type DamageType = "bludgeoning" | "piercing" | "slashing";

// A wielded weapon. Melee weapons attack with Strength, ranged with Dexterity; the same modifier
// goes to the damage roll. The proficiency bonus is added only with the weapon's category.
// TODO: Finesse, Versatile, Heavy, and Ammunition, once something needs them.
export abstract class Weapon extends Equippable {
  readonly slot = "hands";
  abstract readonly category: WeaponCategory;
  abstract readonly ranged: boolean;
  abstract readonly damage: DiceRoll;
  abstract readonly damageType: DamageType;

  constructor() {
    super();
    this.provide(AttackBaseContributor, {
      apply: (attacks, owner) => {
        const scores = owner.get(AbilityScore).scores;
        const modifier = abilityModifier(this.ranged ? scores.dexterity : scores.strength);
        const proficient = owner.get(WeaponTraining).categories.has(this.category);
        attacks.list.push({
          weapon: this,
          ranged: this.ranged,
          bonus: modifier + (proficient ? owner.get(Level).proficiencyBonus : 0),
          damage: this.damage,
          damageBonus: modifier,
          damageType: this.damageType,
        });
      },
    });
  }
}
