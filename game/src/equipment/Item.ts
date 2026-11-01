import { Composite, RegistryKey } from "@atelier/core/composite/Composite";

// The list of known items. Content registers into it with `@Aspect.Variant(Item, name)`.
export const Item = new RegistryKey<Composite>("Item");

/** Where an item is worn or held. `armor` takes one piece; `hands` takes two hands' worth. */
export type Slot = "armor" | "hands";

export const SlotCapacity: Record<Slot, number> = { armor: 1, hands: 2 };

// An item that can be put into a slot of the Inventory's `equipment`. It contributes to stats
// simply by being there: carried items are never visited, so it does not check whether it is in use.
// `slots` is how much of the slot it fills: a one-handed weapon or a Shield fills one of the two
// hands, a two-handed weapon both.
export abstract class Equippable extends Composite {
  abstract readonly slot: Slot;
  readonly slots: number = 1;
}
