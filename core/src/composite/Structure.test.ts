import assert from "node:assert/strict";
import { test } from "node:test";
import { Aspect, RegistryKey } from "./Composite";
import { AspectHolder, Holder, Id } from "./Aspects";
import { Structure } from "./Structure";
import { Property, deserialize, serialize } from "../serial/Data";

@Aspect.Final("Health")
class Health {
  @Property.Primitive value = 10;
}

interface Weapon {
  damage: number;
}
const Weapon = new RegistryKey<Weapon>("Weapon");

@Aspect.Variant(Weapon, "Sword")
class Sword implements Weapon {
  @Property.Primitive damage = 8;
}

@Aspect.Variant(Weapon, "Bow")
class Bow implements Weapon {
  @Property.Primitive damage = 6;
  @Property.Primitive range = 80;
}

const Creature = Structure(
  [Id, () => new Id()],
  [Holder, () => new AspectHolder()],
  [Health],
  [Weapon],
);

function roundTrip(source: InstanceType<typeof Creature>) {
  return deserialize(serialize(source), Creature);
}

test("persisted aspects survive a round trip", () => {
  const source = new Creature();
  source.get(Id).value = "a";
  source.provide(Health, Object.assign(new Health(), { value: 3 }));

  const restored = roundTrip(source);

  assert.equal(restored.get(Id).value, "a");
  assert.equal(restored.get(Health).value, 3);
});

test("aspects with no @Property fields are transient and rebuilt from their factory", () => {
  const source = new Creature();
  source.provide(Health, new Health());

  assert.equal("Holder" in (serialize(source).aspects as object), false);

  assert.ok(roundTrip(source).get(Holder) instanceof AspectHolder);
});

test("a polymorphic aspect restores its concrete implementation", () => {
  for (const weapon of [new Sword(), new Bow()]) {
    const source = new Creature();
    source.provide(Weapon, weapon);

    assert.deepEqual(roundTrip(source).get(Weapon), weapon);
    assert.equal(roundTrip(source).get(Weapon).constructor, weapon.constructor);
  }
});

test("serialized form is plain JSON keyed by aspect name", () => {
  const source = new Creature();
  source.get(Id).value = "a";
  source.provide(Health, Object.assign(new Health(), { value: 3 }));
  source.provide(Weapon, new Bow());

  const json = JSON.parse(JSON.stringify(serialize(source)));

  assert.deepEqual(json, {
    aspects: {
      Id: { value: "a" },
      Health: { value: 3 },
      Weapon: { $type: "Bow", damage: 6, range: 80 },
    },
  });
  assert.deepEqual(deserialize(json, Creature), source);
});

test("validate reports missing aspects that have no factory", () => {
  const creature = new Creature();
  assert.equal(creature.validate(), false);

  creature.provide(Health, new Health());
  creature.provide(Weapon, new Sword());
  assert.equal(creature.validate(), true);
});

test("duplicate aspect names are rejected", () => {
  assert.throws(() => Structure([Health], [Health]), /duplicate aspect name "Health"/);
});

test("deserializing an aspect the structure does not declare throws", () => {
  const Other = Structure([Id, () => new Id()]);
  const source = new Creature();
  source.provide(Health, new Health());

  assert.throws(() => deserialize(serialize(source), Other), /Unknown aspect "Health"/);
});
