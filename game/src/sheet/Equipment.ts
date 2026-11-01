import { Aspect } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import { Item, type Equippable } from "../equipment/Item";

@Aspect.Final("Equipment")
export class Equipment {
  @Property.List(Item.typeMap)
  items: Equippable[] = [];

  /** One item per slot: a second suit of armor or Shield is refused. Caller refreshes the sheet. */
  equip(item: Equippable): void {
    if (!this.items.includes(item)) throw new Error("Item is not in the inventory");
    if (this.items.some((other) => other.equipped && other.slot === item.slot)) {
      throw new Error(`Something is already equipped in the ${item.slot} slot`);
    }
    item.equipped = true;
  }

  unequip(item: Equippable): void {
    item.equipped = false;
  }
}
