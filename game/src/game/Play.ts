import { type Composite } from "@atelier/core/composite/Composite";
import { Rng } from "../dice/Rng";
import { Actions, type Action } from "../stats/Actions";
import { Attacks } from "../stats/Attacks";
import { AttackInteraction } from "./AttackInteraction";
import { Interactions } from "./Interactions";
import { LongRestInteraction } from "./LongRestInteraction";
import { Roster } from "./Roster";
import { ShortRestInteraction } from "./ShortRestInteraction";

// What every creature can do. Features add the rest through `ActionContributor`.
const baseActions: Action[] = [
  {
    name: "Attack",
    exists: (sheet) => sheet.get(Attacks).list.length > 0,
    start: () => new AttackInteraction(),
  },
  { name: "Short Rest", exists: () => true, start: () => new ShortRestInteraction() },
  { name: "Long Rest", exists: () => true, start: () => new LongRestInteraction() },
];

function available(game: Composite, actor: string): Action[] {
  const sheet = game.get(Roster).get(actor);
  return [...baseActions, ...sheet.get(Actions).list].filter((action) => action.exists(sheet));
}

export function listActions(game: Composite, actor: string): string[] {
  return available(game, actor).map((action) => action.name);
}

/** Start an action, which opens an interaction (or finishes at once if it needs nothing). */
export function act(game: Composite, actor: string, name: string): void {
  const action = available(game, actor).find((a) => a.name === name);
  if (!action) throw new Error(`"${name}" is not an action ${actor} has right now`);
  const sheet = game.get(Roster).get(actor);
  game.get(Interactions).start(game, actor, action.start(sheet));
}

export function fill(game: Composite, id: string, key: string, value: string | number): void {
  game.get(Interactions).get(id).fill(game, key, value);
}

/** Fill every open roll from the game's own dice, until none are left. */
export function autoRoll(game: Composite): void {
  for (;;) {
    const open = game
      .get(Interactions)
      .list.flatMap((p) => p.requirements.map((r) => ({ p, r })))
      .find(({ r }) => r.kind === "roll" && r.value === undefined);
    if (!open || open.r.kind !== "roll") return;
    fill(game, open.p.id, open.r.key, game.get(Rng).roll(open.r.dice));
  }
}
