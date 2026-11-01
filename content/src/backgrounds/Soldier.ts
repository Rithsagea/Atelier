import { AspectHolder, Holder } from "@atelier/core/composite/Aspects";
import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { Structure } from "@atelier/core/composite/Structure";
import { Property } from "@atelier/core/serial/Data";
import {
  Background,
  BackgroundEquipment,
  BackgroundFeat,
  BackgroundSkills,
  BackgroundTool,
} from "@atelier/game/backgrounds/Background";
import {
  AbstractBackgroundAbilities,
  BackgroundAbilities,
} from "@atelier/game/backgrounds/BackgroundAbilities";
import { SkillProficiencyContributor } from "@atelier/game/stats/SkillProficiency";

// Strength, Dexterity, or Constitution.
@Aspect.Variant(BackgroundAbilities, "Soldier")
export class SoldierAbilities extends AbstractBackgroundAbilities<
  "strength" | "dexterity" | "constitution"
> {
  readonly abilities = ["strength", "dexterity", "constitution"] as const;
}

// Savage Attacker.
// TODO: a feat object (from the Feat list), added to the sheet's Feats, not a name. Needs
// Attacks to exist first.
@Aspect.Variant(BackgroundFeat, "Soldier")
export class SoldierFeat extends Composite {
  readonly feat = "Savage Attacker";
}

@Aspect.Variant(BackgroundSkills, "Soldier")
export class SoldierSkills extends Composite {
  constructor() {
    super();
    this.provide(SkillProficiencyContributor, {
      apply: (proficiency) => {
        proficiency.skills.add("athletics").add("intimidation");
      },
    });
  }
}

// One kind of Gaming Set.
// TODO: a tool object, written to the inventory when the background is applied, not a name.
// Needs an inventory (Inventory) aspect.
@Aspect.Variant(BackgroundTool, "Soldier")
export class SoldierTool extends Composite {
  @Property.Primitive
  gamingSet?: string;
}

// Package A (spear, shortbow, arrows, gaming set, healer's kit, quiver, clothes, 14 GP) or
// package B (50 GP).
// TODO: write the chosen package's items to the inventory when the background is applied;
// only the choice is kept. Needs an inventory (Inventory) aspect.
@Aspect.Variant(BackgroundEquipment, "Soldier")
export class SoldierEquipment extends Composite {
  @Property.Primitive
  choice?: "A" | "B";
}

@Aspect.Variant(Background, "Soldier")
export class Soldier extends Structure(
  [Holder, () => new AspectHolder()],
  [BackgroundAbilities, () => new SoldierAbilities()],
  [BackgroundFeat, () => new SoldierFeat()],
  [BackgroundSkills, () => new SoldierSkills()],
  [BackgroundTool, () => new SoldierTool()],
  [BackgroundEquipment, () => new SoldierEquipment()],
) {}
