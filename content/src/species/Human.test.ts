import assert from "node:assert/strict";
import { test } from "node:test";
import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import { Class } from "@atelier/game/classes/Class";
import { Sheet, refresh } from "@atelier/game/sheet/Sheet";
import { Background } from "@atelier/game/backgrounds/Background";
import { Species } from "@atelier/game/species/Species";
import { BaseAbilityScore, StaticAbilityScore } from "@atelier/game/stats/BaseAbilityScore";
import { Level } from "@atelier/game/stats/Level";
import { SkillScore } from "@atelier/game/stats/SkillScore";
import { type Skill } from "@atelier/game/stats/Stats";
import { Human } from "./Human";
import { species } from "./index";
import { Skillful } from "./Skillful";
import { Fighter } from "../classes/Fighter";

// Stands in for a real background, so these tests do not depend on one.
@Aspect.Variant(Background, "TestBackground")
class TestBackground extends Composite {}

function build(skill?: Skill) {
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
  const human = new Human();
  human.get(Skillful).skill = skill;
  sheet.provide(Species, human);
  sheet.provide(Background, new TestBackground());
  sheet.provide(Class, new Fighter());
  return sheet;
}

function skillsOf(sheet: InstanceType<typeof Sheet>) {
  refresh(sheet);
  return sheet.get(SkillScore).scores;
}

test("every listed species is registered in game's Species list", () => {
  for (const s of species) assert.ok(Species.typeMap.inverse().get(s));
});

test("Skillful adds the proficiency bonus to the chosen skill only", () => {
  const skills = skillsOf(build("athletics"));
  assert.equal(skills.athletics, 4); // STR 15 (+2) + proficiency (+2)
  assert.equal(skills.acrobatics, 2); // DEX 14, not proficient
});

test("proficiency follows the owner's level", () => {
  const sheet = build("athletics");
  sheet.get(Level).value = 5;
  assert.equal(skillsOf(sheet).athletics, 5); // +2 + 3
});

test("a sheet with Human round-trips through JSON", () => {
  const sheet = build("stealth");
  const data = JSON.parse(JSON.stringify(serialize(sheet)));
  assert.deepEqual(data.aspects.Species, {
    $type: "Human",
    aspects: { Skillful: { skill: "stealth" } },
  });

  const restored = deserialize(data, Sheet);
  assert.equal(restored.validate(), true);
  assert.deepEqual(skillsOf(restored), skillsOf(sheet));
});

test("Skillful is empty by default and contributes nothing", () => {
  const sheet = build();
  assert.equal(sheet.get(Species).get(Skillful).skill, undefined);
  assert.equal(skillsOf(sheet).athletics, 2); // STR 15, not proficient

  const restored = deserialize(JSON.parse(JSON.stringify(serialize(sheet))), Sheet);
  assert.equal(restored.get(Species).get(Skillful).skill, undefined);
});
