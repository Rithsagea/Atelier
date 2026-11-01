import assert from "node:assert/strict";
import { test } from "node:test";
import { Item } from "@atelier/game/equipment/Item";
import { Equipment } from "@atelier/game/sheet/Equipment";
import { Class } from "@atelier/game/classes/Class";
import { Sheet, refresh } from "@atelier/game/sheet/Sheet";
import { ArmorClass } from "@atelier/game/stats/ArmorClass";
import { BaseAbilityScore, StaticAbilityScore } from "@atelier/game/stats/BaseAbilityScore";
import { Fighter } from "../classes/Fighter";
import { ChainMail } from "./ChainMail";
import { items } from "./index";
import { Shield } from "./Shield";

function armorClassWith(worn: (ChainMail | Shield)[], trained = true) {
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
  const equipment = sheet.get(Equipment);
  equipment.items.push(...worn);
  for (const item of worn) equipment.equip(item);
  refresh(sheet);
  return sheet.get(ArmorClass).value;
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
