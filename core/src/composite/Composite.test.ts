import assert from "node:assert/strict";
import { test } from "node:test";
import { Aspect, Composite, RegistryKey } from "./Composite";

@Aspect.Final("Health")
class Health {
  value = 10;
}

interface Weapon {
  damage: number;
}
const Weapon = new RegistryKey<Weapon>("Weapon");

@Aspect.Variant(Weapon, "Sword")
class Sword implements Weapon {
  damage = 8;
}

class Unregistered {}

test("provide and get by constructor", () => {
  const composite = new Composite();
  const health = new Health();
  composite.provide(Health, health);

  assert.equal(composite.get(Health), health);
  assert.equal(composite.has(Health), true);
});

test("get throws when the aspect is absent, suppose does not", () => {
  const composite = new Composite();

  assert.throws(() => composite.get(Health), /does not have aspect "Health"/);
  assert.equal(composite.suppose(Health), undefined);
  assert.equal(composite.has(Health), false);
});

test("provide and get by key for a polymorphic aspect", () => {
  const composite = new Composite();
  composite.provide(Weapon, new Sword());

  assert.equal(composite.get(Weapon).damage, 8);
  assert.equal(Weapon.typeMap.get("Sword"), Sword);
});

test("registering the same implementation id twice throws", () => {
  assert.throws(
    () =>
      Aspect.Variant(
        Weapon,
        "Sword",
      )(
        class Other implements Weapon {
          damage = 1;
        },
      ),
    /already registered/,
  );
});

test("an unregistered constructor is not an aspect", () => {
  assert.throws(() => new Composite().get(Unregistered), /not registered as an aspect/);
});
