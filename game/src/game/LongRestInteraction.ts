import { Aspect, type Composite } from "@atelier/core/composite/Composite";
import { contribute } from "@atelier/core/composite/Contributor";
import { HitDice } from "../stats/HitDice";
import { HitPoints } from "../stats/HitPoints";
import { HeroicInspiration, LongRestContributor } from "../stats/HeroicInspiration";
import { Resources } from "../stats/Resources";
import { Interaction } from "./Interactions";
import { Pending } from "./Pending";

// Needs nothing, so it finishes as soon as it starts.
@Aspect.Variant(Interaction, "LongRest")
export class LongRestInteraction extends Pending {
  advance(game: Composite): void {
    const sheet = this.sheet(game);
    sheet.get(HitPoints).heal(Infinity);
    sheet.get(HitDice).recover(sheet);
    sheet.get(Resources).longRest();
    contribute(LongRestContributor, sheet, sheet.get(HeroicInspiration));
  }
}
