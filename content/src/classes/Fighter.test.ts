import assert from "node:assert/strict";
import { test } from "node:test";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import { Class } from "@atelier/game/classes/Class";
import { Equipment } from "@atelier/game/sheet/Equipment";
import { Sheet, refresh } from "@atelier/game/sheet/Sheet";
import { ArmorClass } from "@atelier/game/stats/ArmorClass";
import { BaseAbilityScore, StaticAbilityScore } from "@atelier/game/stats/BaseAbilityScore";
import { HitDice } from "@atelier/game/stats/HitDice";
import { HitPoints } from "@atelier/game/stats/HitPoints";
import { Level } from "@atelier/game/stats/Level";
import { SavingThrow } from "@atelier/game/stats/SavingThrow";
import { SkillProficiency } from "@atelier/game/stats/SkillProficiency";
import { ChainMail } from "../items/ChainMail";
import { Shield } from "../items/Shield";
import { classes, fightingStyles } from "./index";
import { Defense } from "./Defense";
import { Fighter } from "./Fighter";
import { FighterSkills } from "./FighterSkills";
import { FightingStyle } from "./FightingStyle";

function build(level = 1) {
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
  sheet.provide(Class, new Fighter());
  sheet.get(Level).value = level;
  refresh(sheet);
  return sheet;
}

const fighterOf = (sheet: InstanceType<typeof Sheet>) =>
  sheet.get(Class) as InstanceType<typeof Fighter>;

test("every listed class and fighting style is registered", () => {
  for (const c of classes) assert.ok(Class.typeMap.inverse().get(c));
  for (const style of fightingStyles) assert.ok(FightingStyle.typeMap.inverse().get(style));
});

test("level 1 Hit Points are 10 plus Constitution, with one d10", () => {
  const sheet = build();
  assert.equal(sheet.get(HitPoints).current(sheet), 11); // 10 + CON 13 (+1)
  assert.equal(sheet.get(HitDice).remaining(sheet, "d10"), 1);
});

test("each later level adds the fixed 6 plus Constitution and one d10", () => {
  const sheet = build(3);
  assert.equal(sheet.get(HitPoints).current(sheet), 25); // 10 + 6 + 6 + 3 × 1
  assert.equal(sheet.get(HitDice).remaining(sheet, "d10"), 3);
});

test("leveling up raises current Hit Points by what the level adds", () => {
  const sheet = build();
  sheet.get(HitPoints).take(sheet, 4);
  sheet.get(Level).value = 2;
  refresh(sheet);
  assert.equal(sheet.get(HitPoints).current(sheet), 14); // 18 max - 4 damage
});

test("Fighter is proficient in Strength and Constitution saves", () => {
  const saves = build().get(SavingThrow).scores;
  assert.equal(saves.strength, 4);
  assert.equal(saves.constitution, 3);
  assert.equal(saves.dexterity, 2);
});

test("a Fighter chooses two different skills from the Fighter list", () => {
  const sheet = build();
  fighterOf(sheet).get(FighterSkills).choose("athletics", "perception");
  refresh(sheet);
  assert.deepEqual([...sheet.get(SkillProficiency).skills].sort(), ["athletics", "perception"]);

  const skills = fighterOf(sheet).get(FighterSkills);
  assert.throws(() => skills.choose("athletics", "athletics"), /different/);
  assert.throws(() => skills.choose("athletics", "arcana"), /not a Fighter skill/);
  assert.deepEqual(skills.skills, ["athletics", "perception"]); // a refused choice changes nothing
});

test("Defense adds 1 AC only while wearing armor", () => {
  const sheet = build();
  fighterOf(sheet).provide(FightingStyle, new Defense());
  const chain = new ChainMail();
  sheet.get(Equipment).items.push(chain);

  refresh(sheet);
  assert.equal(sheet.get(ArmorClass).value, 12); // unarmored

  sheet.get(Equipment).equip(chain);
  refresh(sheet);
  assert.equal(sheet.get(ArmorClass).value, 17);
});

test("a Fighter with chain mail, Shield and Defense has AC 19", () => {
  const sheet = build();
  fighterOf(sheet).provide(FightingStyle, new Defense());
  const [chain, shield] = [new ChainMail(), new Shield()];
  sheet.get(Equipment).items.push(chain, shield);
  sheet.get(Equipment).equip(chain);
  sheet.get(Equipment).equip(shield);
  refresh(sheet);
  assert.equal(sheet.get(ArmorClass).value, 19);
});

test("a Fighter round-trips through JSON", () => {
  const sheet = build(2);
  fighterOf(sheet).get(FighterSkills).choose("athletics", "survival");
  fighterOf(sheet).provide(FightingStyle, new Defense());
  sheet.get(HitPoints).take(sheet, 5);

  const data = JSON.parse(JSON.stringify(serialize(sheet)));
  assert.deepEqual(data.aspects.Class, {
    $type: "Fighter",
    aspects: {
      FighterSkills: { skills: ["athletics", "survival"] },
      FightingStyle: { $type: "Defense" },
    },
  });

  const restored = deserialize(data, Sheet);
  refresh(restored);
  assert.equal(restored.get(HitPoints).current(restored), 13); // 18 max - 5 damage
  assert.deepEqual([...restored.get(SkillProficiency).skills].sort(), ["athletics", "survival"]);
});
