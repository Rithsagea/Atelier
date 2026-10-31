import assert from "node:assert/strict";
import { test } from "node:test";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import {
  Background,
  BackgroundEquipment,
  BackgroundTool,
} from "@atelier/game/backgrounds/Background";
import {
  type AbstractBackgroundAbilities,
  BackgroundAbilities,
} from "@atelier/game/backgrounds/BackgroundAbilities";
import { Sheet, refresh } from "@atelier/game/sheet/Sheet";
import { Species } from "@atelier/game/species/Species";
import { AbilityScore } from "@atelier/game/stats/AbilityScore";
import { BaseAbilityScore, StaticAbilityScore } from "@atelier/game/stats/BaseAbilityScore";
import { SkillScore } from "@atelier/game/stats/Stats";
import { Human } from "../species/Human";
import { Skillful } from "../species/Skillful";
import { backgrounds } from "./index";
import { Soldier, SoldierEquipment, SoldierTool } from "./Soldier";

function build(choose?: (abilities: AbstractBackgroundAbilities) => void) {
  const sheet = new Sheet();
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
  sheet.provide(Species, new Human());
  const soldier = new Soldier();
  if (choose) choose(soldier.get(BackgroundAbilities));
  sheet.provide(Background, soldier);
  return sheet;
}

test("every listed background is registered in game's Background list", () => {
  for (const b of backgrounds) assert.ok(Background.typeMap.inverse().get(b));
});

test("two abilities raise the first by 2 and the second by 1", () => {
  const sheet = build((a) => a.raise("strength", "constitution"));
  refresh(sheet);
  const { scores } = sheet.get(AbilityScore);
  assert.equal(scores.strength, 17);
  assert.equal(scores.dexterity, 14);
  assert.equal(scores.constitution, 14);
});

test("three abilities raise each by 1", () => {
  const sheet = build((a) => a.raiseAll());
  refresh(sheet);
  const { scores } = sheet.get(AbilityScore);
  assert.deepEqual([scores.strength, scores.dexterity, scores.constitution], [16, 15, 14]);
});

test("raise rejects naming the same ability twice", () => {
  assert.throws(() => build((a) => a.raise("strength", "strength")), /different abilities/);
});

test("duplicate increases in saved data are rejected on load", () => {
  const data = JSON.parse(JSON.stringify(serialize(build((a) => a.raiseAll()))));
  data.aspects.Background.aspects.BackgroundAbilities.increases = {
    type: "heterogeneous",
    double: "strength",
    single: "strength",
  };
  assert.throws(() => deserialize(data, Sheet), /different abilities/);
});

test("ability increases are empty by default and add nothing", () => {
  const sheet = build();
  refresh(sheet);
  assert.equal(sheet.get(AbilityScore).scores.strength, 15);
});

test("Soldier is proficient in Athletics and Intimidation", () => {
  const sheet = build();
  refresh(sheet);
  const { scores } = sheet.get(SkillScore);
  assert.equal(scores.athletics, 4); // STR 15 (+2) + proficiency (+2)
  assert.equal(scores.intimidation, 1); // CHA 8 (-1) + proficiency (+2)
  assert.equal(scores.stealth, 2); // DEX 14, not proficient
});

test("a skill from both Soldier and Skillful counts once", () => {
  const sheet = build();
  sheet.get(Species).get(Skillful).skill = "athletics";
  refresh(sheet);
  assert.equal(sheet.get(SkillScore).scores.athletics, 4);
});

test("a sheet with Soldier round-trips through JSON", () => {
  const sheet = build((a) => a.raiseAll());
  const soldier = sheet.get(Background);
  (soldier.get(BackgroundTool) as SoldierTool).gamingSet = "Dice Set";
  (soldier.get(BackgroundEquipment) as SoldierEquipment).choice = "B";
  const data = JSON.parse(JSON.stringify(serialize(sheet)));
  assert.deepEqual(data.aspects.Background, {
    $type: "Soldier",
    aspects: {
      BackgroundAbilities: {
        $type: "Soldier",
        increases: { type: "homogeneous" },
      },
      BackgroundFeat: { $type: "Soldier" },
      BackgroundSkills: { $type: "Soldier" },
      BackgroundTool: { $type: "Soldier", gamingSet: "Dice Set" },
      BackgroundEquipment: { $type: "Soldier", choice: "B" },
    },
  });

  const restored = deserialize(data, Sheet);
  assert.equal(restored.validate(), true);
  assert.ok(restored.get(Background).get(BackgroundTool) instanceof SoldierTool);
  assert.ok(restored.get(Background).get(BackgroundEquipment) instanceof SoldierEquipment);
  refresh(restored);
  assert.equal(restored.get(AbilityScore).scores.strength, 16);
});
