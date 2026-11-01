import assert from "node:assert/strict";
import { test } from "node:test";
import { Id } from "@atelier/core/composite/Aspects";
import { Aspect } from "@atelier/core/composite/Composite";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import { Sheet } from "../sheet/Sheet";
import { HeroicInspiration } from "../stats/HeroicInspiration";
import { Game } from "./Game";
import { Interaction, Interactions } from "./Interactions";
import { Pending } from "./Pending";
import { Roster } from "./Roster";

const finished: number[] = [];

// Asks for a pick, then a d6, and records the d6.
@Aspect.Variant(Interaction, "TestPending")
class TestPending extends Pending {
  begin(): void {
    this.requirements = [{ key: "pick", kind: "choice", options: ["a", "b"] }];
  }

  advance(): void {
    if (this.stage++ === 0) {
      this.requirements = [{ key: "d6", kind: "roll", dice: { count: 1, sides: 6 } }];
    } else {
      finished.push(this.rolled("d6"));
    }
  }
}

function build() {
  const game = new Game();
  const sheet = new Sheet();
  game.get(Roster).sheets.push(sheet);
  const actor = sheet.get(Id).value;
  game.get(Interactions).start(game, actor, new TestPending());
  return { game, sheet, id: game.get(Interactions).list[0]!.id };
}

test("an interaction advances only when every requirement is filled", () => {
  const { game, id } = build();
  const pending = game.get(Interactions).get(id);
  pending.fill(game, "pick", "a");
  assert.equal(pending.stage, 1);
  assert.deepEqual(pending.requirements, [
    { key: "d6", kind: "roll", dice: { count: 1, sides: 6 } },
  ]);

  pending.fill(game, "d6", 4);
  assert.deepEqual(finished.splice(0), [4]);
  assert.equal(game.get(Interactions).list.length, 0);
});

test("a value outside the requirement is refused and leaves it open", () => {
  const { game, id } = build();
  const pending = game.get(Interactions).get(id);
  assert.throws(() => pending.fill(game, "pick", "c"), /not an option/);
  assert.throws(() => pending.fill(game, "nope", "a"), /no requirement/);
  pending.fill(game, "pick", "b");
  assert.throws(() => pending.fill(game, "d6", 7), /not a roll of 1d6/);
  assert.throws(() => pending.fill(game, "d6", 2.5), /not a roll of 1d6/);
  assert.equal(pending.requirements[0]!.value, undefined);
});

test("open interactions survive a JSON round trip, ids included", () => {
  const { game, id } = build();
  game.get(Interactions).get(id).fill(game, "pick", "a");

  const restored = deserialize(JSON.parse(JSON.stringify(serialize(game))), Game);
  const pending = restored.get(Interactions).get(id);
  assert.ok(pending instanceof TestPending);
  assert.equal(pending.stage, 1);
  assert.equal(restored.get(Interactions).next, 1);
});

test("with Heroic Inspiration a roll waits for keep or reroll", () => {
  const { game, sheet, id } = build();
  const inspiration = sheet.get(HeroicInspiration);
  inspiration.value = true;
  const pending = game.get(Interactions).get(id);
  pending.fill(game, "pick", "a");

  pending.fill(game, "d6", 2);
  assert.equal(pending.stage, 1); // not advanced: the choice is open
  pending.fill(game, "reroll", "reroll");
  assert.equal(inspiration.value, false);
  assert.equal(pending.requirements.length, 1);
  assert.equal(pending.requirements[0]!.value, undefined); // the roll is open again

  pending.fill(game, "d6", 5); // the new result is final: no second offer
  assert.deepEqual(finished.splice(0), [5]);
});

test("keeping the roll keeps Heroic Inspiration", () => {
  const { game, sheet, id } = build();
  sheet.get(HeroicInspiration).value = true;
  const pending = game.get(Interactions).get(id);
  pending.fill(game, "pick", "a");
  pending.fill(game, "d6", 2);
  pending.fill(game, "reroll", "keep");
  assert.deepEqual(finished.splice(0), [2]);
  assert.equal(sheet.get(HeroicInspiration).value, true);
});
