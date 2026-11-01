import { Aspect } from "@atelier/core/composite/Composite";
import { Item } from "@atelier/game/equipment/Item";
import { Weapon } from "@atelier/game/equipment/Weapon";

// 1d6 slashing, Finesse.
// TODO: Light, Nick mastery.
@Aspect.Variant(Item, "Scimitar")
export class Scimitar extends Weapon {
  readonly category = "martial";
  readonly ranged = false;
  readonly finesse = true;
  readonly damage = { count: 1, sides: 6 };
  readonly damageType = "slashing";
}
