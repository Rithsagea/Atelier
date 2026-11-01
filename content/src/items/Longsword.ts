import { Aspect } from "@atelier/core/composite/Composite";
import { Item } from "@atelier/game/equipment/Item";
import { Weapon } from "@atelier/game/equipment/Weapon";

// 1d8 slashing.
// TODO: Versatile (1d10 with two hands), Sap mastery.
@Aspect.Variant(Item, "Longsword")
export class Longsword extends Weapon {
  readonly category = "martial";
  readonly ranged = false;
  readonly damage = { count: 1, sides: 8 };
  readonly damageType = "slashing";
}
