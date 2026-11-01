import { Aspect } from "@atelier/core/composite/Composite";
import { Item } from "@atelier/game/equipment/Item";
import { Weapon } from "@atelier/game/equipment/Weapon";

// 1d6 piercing, range 80/320 (no board yet, so range is not modelled).
// Two-handed. TODO: Ammunition, Vex mastery.
@Aspect.Variant(Item, "Shortbow")
export class Shortbow extends Weapon {
  readonly slots = 2;
  readonly category = "simple";
  readonly ranged = true;
  readonly damage = { count: 1, sides: 6 };
  readonly damageType = "piercing";
}
