import assert from "node:assert/strict";
import { test } from "node:test";
import { AbilityScore, abilityModifier } from "../stats/AbilityScore";
import { SkillProficiency, SkillProficiencyContributor } from "../stats/SkillProficiency";
import { SkillScore } from "../stats/Stats";
import {
  PointBuyAbilityScore,
  StaticAbilityScore,
  BaseAbilityScore,
  POINT_BUY_BUDGET,
} from "../stats/BaseAbilityScore";
import { Holder, Id, Name } from "@atelier/core/composite/Aspects";
import { Level, proficiencyBonus } from "../stats/Level";
import { Aspect, Composite, getAspectKey } from "@atelier/core/composite/Composite";
import { serialize, deserialize } from "@atelier/core/serial/Data";
import { Species } from "../species/Species";
import { Feats } from "./Feats";
import { Sheet, refresh } from "./Sheet";

// Stands in for content, which `game` cannot depend on.
@Aspect("TestSpecies", Species)
class TestSpecies extends Composite {}

const STANDARD_ARRAY = {
  strength: 15,
  dexterity: 14,
  constitution: 13,
  intelligence: 12,
  wisdom: 10,
  charisma: 8,
};

test("sheet provides Id and Holder", () => {
  const sheet = new Sheet();
  assert.equal(typeof sheet.get(Id).value, "string");
  assert.equal(sheet.has(Holder), true);
});

test("sheet validate reflects BaseAbilityScore presence", () => {
  const sheet = new Sheet();
  assert.equal(sheet.validate(), false);
  sheet.provide(BaseAbilityScore, new PointBuyAbilityScore());
  assert.equal(sheet.validate(), false);
  sheet.provide(Species, new TestSpecies());
  assert.equal(sheet.validate(), true);
});

test("refresh via Holder applies point buy contributions", () => {
  const sheet = new Sheet();
  sheet.provide(BaseAbilityScore, new PointBuyAbilityScore());
  refresh(sheet);
  assert.equal(sheet.get(AbilityScore).scores.strength, 8);
});

test("refresh via Holder applies static contributions", () => {
  const sheet = new Sheet();
  sheet.provide(BaseAbilityScore, new StaticAbilityScore(STANDARD_ARRAY));
  refresh(sheet);
  const abilities = sheet.get(AbilityScore);
  assert.equal(abilities.scores.strength, 15);
  assert.equal(abilities.scores.charisma, 8);
});

test("abilityModifier computes 5e modifier", () => {
  assert.deepEqual([8, 10, 14, 15].map(abilityModifier), [-1, 0, 2, 2]);
});

test("SkillScore refresh applies ability modifiers to governed skills", () => {
  const sheet = new Sheet();
  sheet.provide(BaseAbilityScore, new StaticAbilityScore(STANDARD_ARRAY));
  refresh(sheet);

  const skills = sheet.get(SkillScore);
  assert.equal(skills.scores.athletics, 2); // STR 15
  assert.equal(skills.scores.acrobatics, 2); // DEX 14
  assert.equal(skills.scores.stealth, 2); // DEX 14
  assert.equal(skills.scores.deception, -1); // CHA 8
  assert.equal(skills.scores.insight, 0); // WIS 10
});

test("point buy: default costs nothing, setting spends, lowering refunds", () => {
  const scores = new PointBuyAbilityScore();
  assert.equal(scores.spent(), 0);
  assert.equal(scores.remaining(), POINT_BUY_BUDGET);

  scores.set("strength", 7);
  scores.set("strength", 2);
  assert.equal(scores.spent(), 2);
});

test("point buy: invalid cost and over-budget throw", () => {
  const scores = new PointBuyAbilityScore();
  assert.throws(() => scores.set("strength", 6));
  assert.throws(() => scores.set("strength", -1));

  scores.set("strength", 9);
  scores.set("dexterity", 9);
  scores.set("constitution", 9);
  assert.throws(() => scores.set("intelligence", 9), /Not enough points/);
});

test("BaseAbilityScore typemap resolves impls by tag", () => {
  const typeMap = BaseAbilityScore.typeMap!;
  assert.equal(typeMap.get("PointBuy"), PointBuyAbilityScore);
  assert.equal(typeMap.get("Static"), StaticAbilityScore);
  assert.equal(typeMap.inverse().get(PointBuyAbilityScore), "PointBuy");
  assert.equal(typeMap.inverse().get(StaticAbilityScore), "Static");
});

test("an implementation registered under a key is not itself an aspect", () => {
  assert.doesNotThrow(() => getAspectKey(Id));
  assert.throws(() => getAspectKey(PointBuyAbilityScore));
});

test("sheet serializes point buy round-trip through JSON", () => {
  const sheet = new Sheet();
  const points = new PointBuyAbilityScore();
  sheet.provide(BaseAbilityScore, points);
  sheet.provide(Species, new TestSpecies());
  points.set("strength", 9);
  points.set("dexterity", 5);

  const data = JSON.parse(JSON.stringify(serialize(sheet)));
  assert.deepEqual(data, {
    aspects: {
      Id: { value: sheet.get(Id).value },
      Name: { value: "" },
      Level: { value: 1 },
      Feats: { list: [] },
      Species: { $type: "TestSpecies" },
      BaseAbilityScore: {
        $type: "PointBuy",
        points: {
          strength: 9,
          dexterity: 5,
          constitution: 0,
          intelligence: 0,
          wisdom: 0,
          charisma: 0,
        },
      },
    },
  });

  const restored = deserialize(data, Sheet);
  assert.equal(restored.validate(), true);
  assert.equal(restored.get(Id).value, sheet.get(Id).value);
  assert.deepEqual((restored.get(BaseAbilityScore) as PointBuyAbilityScore).points, points.points);
});

test("sheet serializes static score round-trip through JSON", () => {
  const sheet = new Sheet();
  sheet.provide(BaseAbilityScore, new StaticAbilityScore(STANDARD_ARRAY));
  sheet.provide(Species, new TestSpecies());

  const data = JSON.parse(JSON.stringify(serialize(sheet)));
  assert.deepEqual(data.aspects.BaseAbilityScore, { $type: "Static", scores: STANDARD_ARRAY });

  const restored = deserialize(data, Sheet);
  assert.equal(restored.validate(), true);
  assert.deepEqual((restored.get(BaseAbilityScore) as StaticAbilityScore).scores, STANDARD_ARRAY);
});

test("computed AbilityScore and SkillScore are transient and rebuilt on load", () => {
  const sheet = new Sheet();
  sheet.provide(BaseAbilityScore, new PointBuyAbilityScore());
  const aspects = serialize(sheet).aspects as object;

  for (const name of ["AbilityScore", "SkillScore", "SkillProficiency", "Holder"]) {
    assert.equal(name in aspects, false);
  }

  const restored = deserialize(serialize(sheet), Sheet);
  assert.equal(restored.has(AbilityScore), true);
  assert.equal(restored.has(SkillScore), true);
});

test("derived stats recompute identically after a round trip", () => {
  const original = new Sheet();
  const points = new PointBuyAbilityScore();
  original.provide(BaseAbilityScore, points);
  points.set("strength", 9);
  refresh(original);
  const before = original.get(AbilityScore);

  const restored = deserialize(JSON.parse(JSON.stringify(serialize(original))), Sheet);
  refresh(restored);
  const after = restored.get(AbilityScore);

  assert.deepEqual(after.scores, before.scores);
});

test("deserialize unknown $type throws", () => {
  const data = { aspects: { BaseAbilityScore: { $type: "Bogus", points: {} } } };
  assert.throws(() => deserialize(data, Sheet), /Bogus/);
});

test("deserialize without BaseAbilityScore leaves sheet invalid", () => {
  const sheet = deserialize({ aspects: { Id: { value: "abc" } } }, Sheet);
  assert.equal(sheet.validate(), false);
  assert.equal(sheet.get(Id).value, "abc");
});

test("proficiency bonus steps up every four levels", () => {
  assert.deepEqual([1, 4, 5, 8, 9, 13, 17, 20].map(proficiencyBonus), [2, 2, 3, 3, 4, 5, 6, 6]);
});

test("Name and Level persist through a round trip", () => {
  const sheet = new Sheet();
  sheet.get(Name).value = "Aldric";
  sheet.get(Level).value = 5;
  sheet.provide(BaseAbilityScore, new StaticAbilityScore(STANDARD_ARRAY));

  const restored = deserialize(JSON.parse(JSON.stringify(serialize(sheet))), Sheet);
  assert.equal(restored.get(Name).value, "Aldric");
  assert.equal(restored.get(Level).value, 5);
  assert.equal(restored.get(Level).proficiencyBonus, 3);
});

test("a skill granted by two sources is proficient once", () => {
  const grants = (skill: "athletics" | "stealth") => {
    const source = new Composite();
    source.provide(SkillProficiencyContributor, {
      apply: (proficiency) => void proficiency.skills.add(skill),
    });
    return source;
  };
  const sheet = new Sheet();
  sheet.provide(BaseAbilityScore, new StaticAbilityScore(STANDARD_ARRAY));
  sheet.provide(Species, new TestSpecies());
  sheet.get(Feats).list.push(grants("athletics"), grants("athletics"), grants("stealth"));
  refresh(sheet);

  const skills = sheet.get(SkillScore);
  assert.deepEqual([...sheet.get(SkillProficiency).skills].sort(), ["athletics", "stealth"]);
  assert.equal(skills.scores.athletics, 4); // STR 15 (+2) + proficiency once (+2)
  assert.equal(skills.scores.stealth, 4); // DEX 14 (+2) + proficiency (+2)
  assert.equal(skills.scores.acrobatics, 2); // not proficient
});

test("refresh recomputes from scratch, so repeating it changes nothing", () => {
  const sheet = new Sheet();
  sheet.provide(BaseAbilityScore, new StaticAbilityScore(STANDARD_ARRAY));
  refresh(sheet);
  const once = { ...sheet.get(SkillScore).scores };
  refresh(sheet);
  assert.deepEqual(sheet.get(SkillScore).scores, once);
});
