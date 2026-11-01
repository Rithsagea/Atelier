import { Aspect } from "@atelier/core/composite/Composite";
import { Equippable, Item } from "@atelier/game/equipment/Item";
import { ArmorTraining } from "@atelier/game/stats/ArmorTraining";
import { ArmorClassContributor } from "@atelier/game/stats/ArmorClass";

// +2 AC while equipped, and only with Shield training.
@Aspect.Variant(Item, "Shield")
export class Shield extends Equippable {
  readonly slot = "hands";

  constructor() {
    super();
    this.provide(ArmorClassContributor, {
      apply: (ac, owner) => {
        if (owner.get(ArmorTraining).categories.has("shield")) ac.bonus += 2;
      },
    });
  }
}
