import { Aspect, type Composite } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import { type Equippable, Item, SlotCapacity } from "../equipment/Item";

// Everything a creature owns, in two containers of the one aspect: `items` is carried (any item,
// worn or not) and `equipment` is what is in use, within each slot's capacity. Moving an item from
// one to the other is how it is equipped. Only `equipment` contributes to stats.
@Aspect.Final("Inventory")
export class Inventory {
  @Property.List(Item.typeMap)
  items: Composite[] = [];

  @Property.List(Item.typeMap)
  equipment: Equippable[] = [];

  /** Move a carried item into `equipment`; it must fit its slot. Caller refreshes the sheet. */
  equip(item: Equippable): void {
    const index = this.items.indexOf(item);
    if (index < 0) throw new Error("Item is not in the inventory");
    const used = this.equipment
      .filter((other) => other.slot === item.slot)
      .reduce((sum, other) => sum + other.slots, 0);
    if (used + item.slots > SlotCapacity[item.slot]) {
      throw new Error(`No room in the ${item.slot} slot`);
    }
    this.items.splice(index, 1);
    this.equipment.push(item);
  }

  /** Move an item from `equipment` back into the carried `items`. */
  unequip(item: Equippable): void {
    const index = this.equipment.indexOf(item);
    if (index < 0) throw new Error("Item is not equipped");
    this.equipment.splice(index, 1);
    this.items.push(item);
  }
}
