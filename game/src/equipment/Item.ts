import { Composite, RegistryKey } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";

// The list of known items. Content registers into it with `@Aspect.Variant(Item, name)`.
export const Item = new RegistryKey<Equippable>("Item");

/** What a creature can wear or wield at once: one item per slot. */
export type Slot = "armor" | "shield" | "weapon";

// An item that is worn or held. Whether it is in use lives on the item, so it cannot drift out
// of step with the inventory; a subclass contributes to stats only while `equipped`.
export abstract class Equippable extends Composite {
  abstract readonly slot: Slot;

  @Property.Primitive
  equipped: boolean = false;
}
