import { Composite } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import { type DiceRoll } from "../dice/Rng";
import { HeroicInspiration } from "../stats/HeroicInspiration";
import { Interactions } from "./Interactions";
import { Roster } from "./Roster";

// A value the interaction needs before it can go on. Open until `value` is set; anyone may fill it
// (a player, the DM, a bot, a dice source). A `roll` is a number in range of `dice`. A `choice` is
// one of `options`; one with `of` decides whether to reroll the roll of that key.
export type Requirement =
  | { key: string; kind: "choice"; options: string[]; value?: string; of?: string }
  | { key: string; kind: "roll"; dice: DiceRoll; value?: number };

// Something started that is not finished: it holds the requirements of its current stage and,
// once all are filled, `advance` applies that stage's effect and either opens the next
// stage's requirements or leaves them filled to finish. Actions only start one; every effect
// happens in `advance`.
export abstract class Pending extends Composite {
  @Property.Primitive
  id: string = "";

  @Property.Primitive
  actor: string = "";

  @Property.Primitive
  stage: number = 0;

  @Property.Primitive
  requirements: Requirement[] = [];

  /** Open the first stage's requirements. */
  begin(_game: Composite): void {}

  abstract advance(game: Composite): void;

  fill(game: Composite, key: string, value: string | number): void {
    const requirement = this.requirements.find((r) => r.key === key);
    if (!requirement) throw new Error(`Interaction ${this.id} has no requirement "${key}"`);
    if (requirement.kind === "choice") {
      if (!requirement.options.includes(String(value))) {
        throw new Error(`"${value}" is not an option for "${key}"`);
      }
      requirement.value = String(value);
    } else {
      const { count, sides } = requirement.dice;
      if (
        typeof value !== "number" ||
        !Number.isInteger(value) ||
        value < count ||
        value > count * sides
      ) {
        throw new Error(`${value} is not a roll of ${count}d${sides}`);
      }
      requirement.value = value;
    }

    const inspiration = this.sheet(game).get(HeroicInspiration);
    if (requirement.kind === "roll" && inspiration.value) {
      this.requirements.push({
        key: "reroll",
        kind: "choice",
        options: ["keep", "reroll"],
        of: key,
      });
      return;
    }
    if (requirement.kind === "choice" && requirement.of) {
      this.requirements.splice(this.requirements.indexOf(requirement), 1);
      if (requirement.value === "reroll") {
        inspiration.value = false;
        delete this.requirements.find((r) => r.key === requirement.of)!.value;
      }
    }
    this.settle(game);
  }

  /** Advance once nothing is open, and drop the interaction if that opened nothing new. */
  settle(game: Composite): void {
    if (this.requirements.some((r) => r.value === undefined)) return;
    this.advance(game);
    if (this.requirements.every((r) => r.value !== undefined)) game.get(Interactions).remove(this);
  }

  protected sheet(game: Composite): Composite {
    return game.get(Roster).get(this.actor);
  }

  protected rolled(key: string): number {
    const requirement = this.requirements.find((r) => r.key === key);
    if (requirement?.kind !== "roll" || requirement.value === undefined) {
      throw new Error(`"${key}" has not been rolled`);
    }
    return requirement.value;
  }

  protected chosen(key: string): string {
    const requirement = this.requirements.find((r) => r.key === key);
    if (requirement?.kind !== "choice" || requirement.value === undefined) {
      throw new Error(`"${key}" has not been chosen`);
    }
    return requirement.value;
  }
}
