import { Id } from "@atelier/core/composite/Aspects";
import { Aspect, type Composite } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import { ArmorClass } from "../stats/ArmorClass";
import { Attacks } from "../stats/Attacks";
import { HitPoints } from "../stats/HitPoints";
import { Interaction } from "./Interactions";
import { Pending } from "./Pending";
import { Roster } from "./Roster";

// Choose an attack and a target, roll to hit, and on a hit roll damage (doubled dice on a critical
// hit). A natural 1 always misses; a roll at or above the attack's `critical` always hits.
@Aspect.Variant(Interaction, "Attack")
export class AttackInteraction extends Pending {
  @Property.Primitive
  attack: number = 0;

  @Property.Primitive
  target: string = "";

  @Property.Primitive
  critical: boolean = false;

  begin(game: Composite): void {
    const targets = game.get(Roster).sheets.map((s) => s.get(Id).value);
    this.requirements = [
      {
        key: "attack",
        kind: "choice",
        options: this.sheet(game)
          .get(Attacks)
          .list.map((_, i) => String(i)),
      },
      { key: "target", kind: "choice", options: targets.filter((id) => id !== this.actor) },
    ];
  }

  advance(game: Composite): void {
    const target = () => game.get(Roster).get(this.target);
    const attack = () => this.sheet(game).get(Attacks).list[this.attack]!;
    switch (this.stage++) {
      case 0:
        this.attack = Number(this.chosen("attack"));
        this.target = this.chosen("target");
        this.requirements = [{ key: "d20", kind: "roll", dice: { count: 1, sides: 20 } }];
        break;
      case 1: {
        const natural = this.rolled("d20");
        this.critical = natural >= attack().critical;
        const hit =
          this.critical ||
          (natural > 1 && natural + attack().bonus >= target().get(ArmorClass).value);
        if (hit) {
          const { count, sides } = attack().damage;
          const dice = { count: this.critical ? count * 2 : count, sides };
          this.requirements = [{ key: "damage", kind: "roll", dice }];
        }
        break;
      }
      default:
        target()
          .get(HitPoints)
          .take(target(), Math.max(0, this.rolled("damage") + attack().damageBonus));
    }
  }
}
