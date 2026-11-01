import assert from "node:assert/strict";
import { test } from "node:test";
import { Item } from "@atelier/game/equipment/Item";
import { Inventory } from "@atelier/game/sheet/Inventory";
import { Class } from "@atelier/game/classes/Class";
import { Sheet, refresh } from "@atelier/game/sheet/Sheet";
import { ArmorClass } from "@atelier/game/stats/ArmorClass";
import { Attacks } from "@atelier/game/stats/Attacks";
import { BaseAbilityScore, StaticAbilityScore } from "@atelier/game/stats/BaseAbilityScore";
import { Fighter } from "../classes/Fighter";
import { ChainMail } from "./ChainMail";
import { items } from "./index";
import { Longsword } from "./Longsword";
import { Shield } from "./Shield";
import { Shortbow } from "./Shortbow";

function build(worn: (ChainMail | Shield | Longsword | Shortbow)[], trained = true) {
  const sheet = new Sheet();
  if (trained) sheet.provide(Class, new Fighter());
  sheet.provide(
    BaseAbilityScore,
    new StaticAbilityScore({
      strength: 15,
      dexterity: 14,
      constitution: 13,
      intelligence: 12,
      wisdom: 10,
      charisma: 8,
    }),
  );
  const inventory = sheet.get(Inventory);
  inventory.items.push(...worn);
  for (const item of worn) inventory.equip(item);
  refresh(sheet);
  return sheet;
}

function refreshed(sheet: ReturnType<typeof build>) {
  refresh(sheet);
  return sheet.get(Attacks).list;
}

function armorClassWith(worn: (ChainMail | Shield)[], trained = true) {
  return build(worn, trained).get(ArmorClass).value;
}

test("every listed item is registered in game's Item list", () => {
  for (const item of items) assert.ok(Item.typeMap.inverse().get(item));
});

test("chain mail is AC 16 and a Shield adds 2", () => {
  assert.equal(armorClassWith([]), 12); // 10 + DEX 14
  assert.equal(armorClassWith([new ChainMail()]), 16);
  assert.equal(armorClassWith([new Shield()]), 14);
  assert.equal(armorClassWith([new ChainMail(), new Shield()]), 18);
});

test("a Shield gives no AC without Shield training", () => {
  assert.equal(armorClassWith([new Shield()], false), 12);
  assert.equal(armorClassWith([new ChainMail(), new Shield()], false), 16);
});

test("a trained melee weapon attacks with Strength plus proficiency", () => {
  const [attack, ...rest] = build([new Longsword()]).get(Attacks).list;
  assert.equal(rest.length, 0);
  assert.equal(attack.bonus, 4); // STR 15 (+2) + proficiency +2
  assert.equal(attack.damageBonus, 2);
  assert.deepEqual(attack.damage, { count: 1, sides: 8 });
  assert.equal(attack.damageType, "slashing");
});

test("a ranged weapon attacks with Dexterity", () => {
  const [attack] = build([new Shortbow()]).get(Attacks).list;
  assert.equal(attack.bonus, 4); // DEX 14 (+2) + proficiency +2
  assert.equal(attack.damageBonus, 2);
  assert.equal(attack.ranged, true);
});

test("an untrained weapon attacks without the proficiency bonus", () => {
  const [attack] = build([new Longsword()], false).get(Attacks).list;
  assert.equal(attack.bonus, 2);
  assert.equal(attack.damageBonus, 2);
});

test("only the equipped weapon attacks, and armor adds no attack", () => {
  const sheet = build([new Longsword(), new ChainMail()]);
  assert.equal(sheet.get(Attacks).list.length, 1);
  const bow = new Shortbow();
  const inventory = sheet.get(Inventory);
  inventory.items.push(bow);
  inventory.unequip(inventory.equipment.find((item) => item instanceof Longsword) as Longsword);
  inventory.equip(bow);
  refresh(sheet);
  assert.deepEqual(
    sheet.get(Attacks).list.map((a) => a.weapon),
    [bow],
  );
});

test("hands: a weapon and a Shield, or two one-handed weapons, fit; a two-handed weapon fills both", () => {
  const sheet = build([new Longsword(), new Shield()]);
  assert.equal(sheet.get(Attacks).list.length, 1);

  const inventory = sheet.get(Inventory);
  const [second, bow] = [new Longsword(), new Shortbow()];
  inventory.items.push(second, bow);
  assert.throws(() => inventory.equip(second), /hands slot/); // both hands are in use
  inventory.unequip(inventory.equipment.find((item) => item instanceof Shield) as Shield);
  inventory.equip(second);
  assert.equal(refreshed(sheet).length, 2);

  assert.throws(() => inventory.equip(bow), /hands slot/); // needs both hands
  for (const sword of [...inventory.equipment]) inventory.unequip(sword as Longsword);
  inventory.equip(bow);
  assert.deepEqual(
    refreshed(sheet).map((a) => a.weapon),
    [bow],
  );
  const shield = inventory.items.find((item) => item instanceof Shield) as Shield;
  assert.throws(() => inventory.equip(shield), /hands slot/);
});
