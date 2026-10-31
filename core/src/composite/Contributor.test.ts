import assert from "node:assert/strict";
import { test } from "node:test";
import { AspectHolder, Holder } from "./Aspects";
import { AspectKey, Composite } from "./Composite";
import { ContributorKey, contribute } from "./Contributor";

const Count = ContributorKey<{ n: number }>("Count");
const Slot = new AspectKey<Composite>("Slot");

function adding(amount: number, ...children: Composite[]): Composite {
  const c = new Composite();
  c.provide(Count, { apply: (target) => void (target.n += amount) });
  if (children.length) {
    c.provide(Holder, new AspectHolder());
    c.provide(Slot, children[0]!);
  }
  return c;
}

test("contribute reaches children and their children through each Holder", () => {
  const root = new Composite();
  root.provide(Holder, new AspectHolder());
  root.provide(Slot, adding(2, adding(4)));

  const target = { n: 0 };
  contribute(Count, root, target);
  assert.equal(target.n, 6);
});

test("contribute throws when the owner has no Holder", () => {
  assert.throws(() => contribute(Count, new Composite(), { n: 0 }), /Holder/);
});
