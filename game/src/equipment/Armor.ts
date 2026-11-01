import { type ArmorClass, ArmorClassBaseContributor } from "../stats/ArmorClass";
import { Equippable } from "./Item";

// Body armor. `dexCap` is the most Dexterity modifier it allows: Infinity for light armor, 2 for
// medium, 0 for heavy (which ignores Dexterity).
export abstract class Armor extends Equippable {
  readonly slot = "armor";
  abstract readonly base: number;
  abstract readonly dexCap: number;

  constructor() {
    super();
    this.provide(ArmorClassBaseContributor, {
      apply: (ac: ArmorClass) => {
        if (!this.equipped) return;
        ac.base = this.base;
        ac.dexCap = this.dexCap;
        ac.armor = true;
      },
    });
  }
}
