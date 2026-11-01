import { Aspect } from "@atelier/core/composite/Composite";
import { Armor } from "@atelier/game/equipment/Armor";
import { Item } from "@atelier/game/equipment/Item";

// Light armor: AC 11 plus the Dexterity modifier.
@Aspect.Variant(Item, "LeatherArmor")
export class LeatherArmor extends Armor {
  readonly base = 11;
  readonly dexCap = Infinity;
}
