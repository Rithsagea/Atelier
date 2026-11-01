import { AspectHolder, Holder } from "@atelier/core/composite/Aspects";
import { Aspect } from "@atelier/core/composite/Composite";
import { Structure } from "@atelier/core/composite/Structure";
import { Class } from "@atelier/game/classes/Class";
import { FighterSkills } from "./FighterSkills";
import { FighterTraits } from "./FighterTraits";
import { FighterSubclass } from "./FighterSubclass";
import { FightingStyle } from "./FightingStyle";

// Levels 1-3. The sheet's Level is the Fighter level; the fighting style and subclass are empty
// until chosen.
// TODO: Action Surge, once there is an action economy to grant an extra action in.
@Aspect.Variant(Class, "Fighter")
export class Fighter extends Structure(
  [Holder, () => new AspectHolder()],
  [FighterTraits, () => new FighterTraits()],
  [FighterSkills, () => new FighterSkills()],
  [FightingStyle],
  [FighterSubclass],
) {}
