import { Aspect } from "@atelier/core/composite/Composite";
import { Armor } from "@atelier/game/equipment/Armor";
import { Item } from "@atelier/game/equipment/Item";

// Heavy armor: AC 16, no Dexterity modifier.
// TODO: Strength 13 (speed -10 feet without it) and Stealth disadvantage, once there is a Speed
// and a d20 test to apply them to.
@Aspect.Variant(Item, "ChainMail")
export class ChainMail extends Armor {
  readonly base = 16;
  readonly dexCap = 0;
}
