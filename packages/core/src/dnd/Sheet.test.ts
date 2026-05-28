import { expect, test } from "bun:test";
import { AbilityScore } from "../stats/AbilityScore";
import {
  PointBuyAbilityScore,
  StaticAbilityScore,
  BaseAbilityScore,
  POINT_BUY_BUDGET,
} from "../stats/BaseAbilityScore";
import { Holder } from "../composite/Structure";
import { Id } from "../composite/Id";
import { Aspect, getAspectKey } from "../composite/Composite";
import { serialize, deserialize } from "../serial/Data";
import { Sheet } from "./Sheet";

test("sheet provides Id", () => {
  const sheet = new Sheet();
  expect(sheet.has(Id)).toBe(true);
  expect(typeof sheet.get(Id).value).toBe("string");
});

test("sheet provides Holder", () => {
  const sheet = new Sheet();
  expect(sheet.has(Holder)).toBe(true);
});

test("sheet validate reflects BaseAbilityScore presence", () => {
  const sheet = new Sheet();
  expect(sheet.validate()).toBe(false);
  sheet.provide(BaseAbilityScore, new PointBuyAbilityScore());
  expect(sheet.validate()).toBe(true);
});

test("refresh via Holder applies point buy contributions", () => {
  const sheet = new Sheet();
  sheet.provide(BaseAbilityScore, new PointBuyAbilityScore());
  const score = new AbilityScore();
  sheet.provide(AbilityScore, score);
  score.refresh(sheet);
  expect(score.scores.strength).toBe(8);
});

test("refresh via Holder applies static contributions", () => {
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
  const score = new AbilityScore();
  sheet.provide(AbilityScore, score);
  score.refresh(sheet);
  expect(score.scores.strength).toBe(15);
  expect(score.scores.charisma).toBe(8);
});

test("default base scores cost zero points", () => {
  const scores = new PointBuyAbilityScore();
  expect(scores.spent()).toBe(0);
  expect(scores.remaining()).toBe(POINT_BUY_BUDGET);
});

test("set base score spends points", () => {
  const scores = new PointBuyAbilityScore();
  scores.set("strength", 9);
  expect(scores.spent()).toBe(9);
  expect(scores.remaining()).toBe(POINT_BUY_BUDGET - 9);
});

test("set base score throws when invalid cost", () => {
  const scores = new PointBuyAbilityScore();
  expect(() => scores.set("strength", 6)).toThrow();
  expect(() => scores.set("strength", -1)).toThrow();
});

test("set base score throws when over budget", () => {
  const scores = new PointBuyAbilityScore();
  scores.set("strength", 9);
  scores.set("dexterity", 9);
  scores.set("constitution", 9);
  expect(() => scores.set("intelligence", 9)).toThrow();
});

test("lowering base score refunds points", () => {
  const scores = new PointBuyAbilityScore();
  scores.set("strength", 7);
  scores.set("strength", 2);
  expect(scores.spent()).toBe(2);
});

test("BaseAbilityScore typemap resolves impls by tag", () => {
  expect(BaseAbilityScore.typeMap!.get("PointBuy")).toBe(PointBuyAbilityScore);
  expect(BaseAbilityScore.typeMap!.get("Static")).toBe(StaticAbilityScore);
  expect(BaseAbilityScore.typeMap!.getKey(PointBuyAbilityScore)).toBe("PointBuy");
  expect(BaseAbilityScore.typeMap!.getKey(StaticAbilityScore)).toBe("Static");
});

test("two-arg @Aspect does not populate aspectRegistry", () => {
  expect(() => getAspectKey(Id)).not.toThrow();
  expect(() => getAspectKey(PointBuyAbilityScore)).toThrow();
});

test("duplicate impl registration throws", () => {
  expect(() => Aspect("PointBuy", BaseAbilityScore)(class {})).toThrow();
});

test("single-arg @Aspect sets ctor and leaves typeMap undefined", () => {
  const idKey = getAspectKey(Id);
  expect(idKey.ctor).toBe(Id);
  expect(idKey.typeMap).toBeUndefined();
});

test("sheet serializes point buy round-trip", () => {
  const sheet = new Sheet();
  const points = new PointBuyAbilityScore();
  sheet.provide(BaseAbilityScore, points);
  points.set("strength", 9);
  points.set("dexterity", 5);

  const data = serialize(sheet);
  expect(data).toStrictEqual({
    aspects: {
      Id: { value: sheet.get(Id).value },
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
  expect("Holder" in (data.aspects as object)).toBe(false);

  const restored = deserialize(data, Sheet);
  expect(restored.validate()).toBe(true);
  expect(restored.get(Id).value).toBe(sheet.get(Id).value);
  const restoredPoints = restored.get(BaseAbilityScore) as PointBuyAbilityScore;
  expect(restoredPoints.points.strength).toBe(9);
  expect(restoredPoints.points.dexterity).toBe(5);
});

test("sheet serializes static score round-trip", () => {
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

  const data = serialize(sheet);
  expect((data.aspects as { BaseAbilityScore: unknown }).BaseAbilityScore).toStrictEqual({
    $type: "Static",
    scores: {
      strength: 15,
      dexterity: 14,
      constitution: 13,
      intelligence: 12,
      wisdom: 10,
      charisma: 8,
    },
  });

  const restored = deserialize(data, Sheet);
  expect(restored.validate()).toBe(true);
  const restoredScores = restored.get(BaseAbilityScore) as StaticAbilityScore;
  expect(restoredScores.scores.strength).toBe(15);
  expect(restoredScores.scores.charisma).toBe(8);
});

test("refresh works on deserialized point buy sheet", () => {
  const original = new Sheet();
  original.provide(BaseAbilityScore, new PointBuyAbilityScore());
  (original.get(BaseAbilityScore) as PointBuyAbilityScore).set("strength", 9);
  const before = new AbilityScore();
  original.provide(AbilityScore, before);
  before.refresh(original);

  const restored = deserialize(serialize(original), Sheet);
  const after = new AbilityScore();
  restored.provide(AbilityScore, after);
  after.refresh(restored);

  expect(after.scores).toStrictEqual(before.scores);
});

test("deserialize unknown $type throws", () => {
  expect(() =>
    deserialize(
      { aspects: { BaseAbilityScore: { $type: "Bogus", points: {} } } },
      Sheet,
    ),
  ).toThrow(/Bogus/);
});

test("deserialize without BaseAbilityScore leaves sheet invalid", () => {
  const sheet = deserialize(
    { aspects: { Id: { value: "abc" } } },
    Sheet,
  );
  expect(sheet.validate()).toBe(false);
  expect(sheet.get(Id).value).toBe("abc");
});
