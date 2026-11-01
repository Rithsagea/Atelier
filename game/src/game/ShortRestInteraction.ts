import { Aspect, type Composite } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import { abilityModifier, AbilityScore } from "../stats/AbilityScore";
import { Dice, HitDice, type Die } from "../stats/HitDice";
import { HitPoints } from "../stats/HitPoints";
import { Resources } from "../stats/Resources";
import { Interaction } from "./Interactions";
import { Pending } from "./Pending";

// Spend some Hit Dice of the smallest size that has any left, roll them, heal the total plus the
// Constitution modifier per die, and regain short-rest resources.
@Aspect.Variant(Interaction, "ShortRest")
export class ShortRestInteraction extends Pending {
  @Property.Primitive
  die?: Die;

  @Property.Primitive
  spent: number = 0;

  begin(game: Composite): void {
    const sheet = this.sheet(game);
    this.die = Dice.find((die) => sheet.get(HitDice).remaining(sheet, die) > 0);
    const left = this.die ? sheet.get(HitDice).remaining(sheet, this.die) : 0;
    const options = Array.from({ length: left + 1 }, (_, i) => String(i));
    this.requirements = [{ key: "spend", kind: "choice", options }];
  }

  advance(game: Composite): void {
    const sheet = this.sheet(game);
    if (this.stage++ === 0) {
      this.spent = Number(this.chosen("spend"));
      if (this.spent > 0) {
        for (let i = 0; i < this.spent; i++) sheet.get(HitDice).spend(sheet, this.die!);
        const dice = { count: this.spent, sides: Number(this.die!.slice(1)) };
        this.requirements = [{ key: "heal", kind: "roll", dice }];
        return;
      }
    } else {
      const constitution = abilityModifier(sheet.get(AbilityScore).scores.constitution);
      sheet.get(HitPoints).heal(Math.max(0, this.rolled("heal") + constitution * this.spent));
    }
    sheet.get(Resources).shortRest(sheet);
  }
}
