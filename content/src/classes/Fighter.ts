import { AspectHolder, Holder } from "@atelier/core/composite/Aspects";
import { Aspect } from "@atelier/core/composite/Composite";
import { Structure } from "@atelier/core/composite/Structure";
import { Class } from "@atelier/game/classes/Class";
import { FighterSkills } from "./FighterSkills";
import { FighterTraits } from "./FighterTraits";
import { FightingStyle } from "./FightingStyle";

// Levels 1-3. The sheet's Level is the Fighter level; the fighting style is empty until chosen.
// TODO: Second Wind and Action Surge (need Resources and actions), the Champion subclass (needs
// Attacks).
@Aspect.Variant(Class, "Fighter")
export class Fighter extends Structure(
  [Holder, () => new AspectHolder()],
  [FighterTraits, () => new FighterTraits()],
  [FighterSkills, () => new FighterSkills()],
  [FightingStyle],
) {}
