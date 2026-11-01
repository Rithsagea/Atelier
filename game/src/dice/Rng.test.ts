import assert from "node:assert/strict";
import { test } from "node:test";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import { Rng } from "./Rng";

function seeded(state: number) {
  const rng = new Rng();
  rng.state = state;
  return rng;
}

test("the same seed rolls the same dice", () => {
  const a = seeded(42);
  const b = seeded(42);
  for (let i = 0; i < 20; i++)
    assert.equal(a.roll({ count: 2, sides: 6 }), b.roll({ count: 2, sides: 6 }));
  assert.notEqual(seeded(1).next(), seeded(2).next());
});

test("rolls stay within the dice's range and reach both ends", () => {
  const rng = seeded(7);
  const seen = new Set<number>();
  for (let i = 0; i < 500; i++) {
    const roll = rng.roll({ count: 1, sides: 20 });
    assert.ok(Number.isInteger(roll) && roll >= 1 && roll <= 20);
    seen.add(roll);
  }
  assert.ok(seen.has(1) && seen.has(20));
  for (let i = 0; i < 200; i++) {
    const total = rng.roll({ count: 3, sides: 6 });
    assert.ok(total >= 3 && total <= 18);
  }
});

test("a restored state continues the same sequence", () => {
  const rng = seeded(99);
  rng.roll({ count: 5, sides: 8 });
  const restored = deserialize(JSON.parse(JSON.stringify(serialize(rng))), Rng);
  for (let i = 0; i < 10; i++) assert.equal(restored.next(), rng.next());
});
