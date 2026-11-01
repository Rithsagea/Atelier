import { Aspect } from "@atelier/core/composite/Composite";
import { Equippable, Item } from "@atelier/game/equipment/Item";
import { type ArmorClass, ArmorClassContributor } from "@atelier/game/stats/ArmorClass";

// +2 AC while held.
// TODO: only with Shield training, once classes grant it.
@Aspect.Variant(Item, "Shield")
export class Shield extends Equippable {
  readonly slot = "shield";

  constructor() {
    super();
    this.provide(ArmorClassContributor, {
      apply: (ac: ArmorClass) => {
        if (this.equipped) ac.bonus += 2;
      },
    });
  }
}
