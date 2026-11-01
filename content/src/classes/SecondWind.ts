import { Aspect, type Composite } from "@atelier/core/composite/Composite";
import { Interaction } from "@atelier/game/game/Interactions";
import { Pending } from "@atelier/game/game/Pending";
import { type Action } from "@atelier/game/stats/Actions";
import { HitPoints } from "@atelier/game/stats/HitPoints";
import { Level } from "@atelier/game/stats/Level";
import { Resources } from "@atelier/game/stats/Resources";

// Regain 1d10 plus the Fighter level in Hit Points.
// TODO: a bonus action, once there is an action economy.
@Aspect.Variant(Interaction, "SecondWind")
export class SecondWindInteraction extends Pending {
  begin(): void {
    this.requirements = [{ key: "heal", kind: "roll", dice: { count: 1, sides: 10 } }];
  }

  advance(game: Composite): void {
    const sheet = this.sheet(game);
    sheet.get(HitPoints).heal(this.rolled("heal") + sheet.get(Level).value);
  }
}

export const secondWind: Action = {
  name: "Second Wind",
  exists: (sheet) => sheet.get(Resources).remaining(sheet, "Second Wind") > 0,
  start(sheet) {
    sheet.get(Resources).spend(sheet, "Second Wind");
    return new SecondWindInteraction();
  },
};
