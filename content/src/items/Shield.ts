import { Aspect, type Composite } from "@atelier/core/composite/Composite";
import { Equippable, Item } from "@atelier/game/equipment/Item";
import { ArmorTraining } from "@atelier/game/stats/ArmorTraining";
import { type ArmorClass, ArmorClassContributor } from "@atelier/game/stats/ArmorClass";

// +2 AC while held, and only with Shield training.
@Aspect.Variant(Item, "Shield")
export class Shield extends Equippable {
  readonly slot = "shield";

  constructor() {
    super();
    this.provide(ArmorClassContributor, {
      apply: (ac: ArmorClass, owner: Composite) => {
        if (this.equipped && owner.get(ArmorTraining).categories.has("shield")) ac.bonus += 2;
      },
    });
  }
}
