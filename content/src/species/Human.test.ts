import assert from "node:assert/strict";
import { test } from "node:test";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import { Sheet } from "@atelier/game/sheet/Sheet";
import { Species } from "@atelier/game/species/Species";
import { AbilityScore } from "@atelier/game/stats/AbilityScore";
import { BaseAbilityScore, StaticAbilityScore } from "@atelier/game/stats/BaseAbilityScore";
import { Level } from "@atelier/game/stats/Level";
import { SkillScore } from "@atelier/game/stats/Stats";
import { Human } from "./Human";
import { species } from "./index";

function build(skill: ConstructorParameters<typeof Human>[0]) {
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
  sheet.provide(Species, new Human(skill));
  return sheet;
}

function skillsOf(sheet: InstanceType<typeof Sheet>) {
  const abilities = new AbilityScore();
  const skills = new SkillScore();
  sheet.provide(AbilityScore, abilities);
  sheet.provide(SkillScore, skills);
  abilities.refresh(sheet);
  skills.refresh(sheet);
  return skills.scores;
}

test("every listed species is registered in game's Species list", () => {
  for (const s of species) assert.ok(Species.typeMap!.inverse().get(s));
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
  assert.deepEqual(data.aspects.Species, { $type: "Human", skill: "stealth" });

  const restored = deserialize(data, Sheet);
  assert.equal(restored.validate(), true);
  assert.deepEqual(skillsOf(restored), skillsOf(sheet));
});
