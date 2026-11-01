import { Aspect } from "@atelier/core/composite/Composite";
import { Item } from "@atelier/game/equipment/Item";
import { Weapon } from "@atelier/game/equipment/Weapon";

// 1d6 piercing, range 80/320 (no board yet, so range is not modelled).
// TODO: Ammunition and Two-Handed, Vex mastery.
@Aspect.Variant(Item, "Shortbow")
export class Shortbow extends Weapon {
  readonly category = "simple";
  readonly ranged = true;
  readonly damage = { count: 1, sides: 6 };
  readonly damageType = "piercing";
}
