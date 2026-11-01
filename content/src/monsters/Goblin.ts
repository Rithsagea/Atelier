import { Name } from "@atelier/core/composite/Aspects";
import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { Class } from "@atelier/game/classes/Class";
import { Inventory } from "@atelier/game/sheet/Inventory";
import { Sheet, refresh } from "@atelier/game/sheet/Sheet";
import { ArmorTrainingContributor } from "@atelier/game/stats/ArmorTraining";
import { BaseAbilityScore, StaticAbilityScore } from "@atelier/game/stats/BaseAbilityScore";
import { MaxHitPointsContributor } from "@atelier/game/stats/HitPoints";
import { WeaponTrainingContributor } from "@atelier/game/stats/WeaponTraining";
import { LeatherArmor } from "../items/LeatherArmor";
import { Scimitar } from "../items/Scimitar";
import { Shield } from "../items/Shield";

// A monster is a sheet whose Class slot holds its stat block: 10 Hit Points, light armor and
// Shield, martial weapons. With leather armor, a Shield and a Scimitar the sheet computes AC 15 and
// +4 to hit for 1d6+2.
// TODO: Nimble Escape.
@Aspect.Variant(Class, "Goblin")
export class Goblin extends Composite {
  constructor() {
    super();
    this.provide(MaxHitPointsContributor, {
      apply: (max) => {
        max.value += 10;
      },
    });
    this.provide(ArmorTrainingContributor, {
      apply: (training) => {
        training.categories.add("light").add("shield");
      },
    });
    this.provide(WeaponTrainingContributor, {
      apply: (training) => {
        training.categories.add("martial");
      },
    });
  }
}

export function createGoblin(): InstanceType<typeof Sheet> {
  const sheet = new Sheet();
  sheet.get(Name).value = "Goblin";
  sheet.provide(
    BaseAbilityScore,
    new StaticAbilityScore({
      strength: 8,
      dexterity: 14,
      constitution: 10,
      intelligence: 10,
      wisdom: 8,
      charisma: 8,
    }),
  );
  sheet.provide(Class, new Goblin());
  const inventory = sheet.get(Inventory);
  for (const item of [new LeatherArmor(), new Shield(), new Scimitar()]) {
    inventory.items.push(item);
    inventory.equip(item);
  }
  refresh(sheet);
  return sheet;
}
