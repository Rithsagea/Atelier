import assert from "node:assert/strict";
import { test } from "node:test";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import { Rng } from "../dice/Rng";
import { Game } from "./Game";

test("a game snapshot keeps rolling the same dice", () => {
  const game = new Game();
  game.get(Rng).state = 1234;
  game.get(Rng).roll({ count: 3, sides: 6 });

  const restored = deserialize(JSON.parse(JSON.stringify(serialize(game))), Game);
  assert.equal(
    restored.get(Rng).roll({ count: 1, sides: 20 }),
    game.get(Rng).roll({ count: 1, sides: 20 }),
  );
});
