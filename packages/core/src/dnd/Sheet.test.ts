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
import { Aspect, getAspectKey, getAspectTypeMap } from "../composite/Composite";
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
  const map = getAspectTypeMap(BaseAbilityScore);
  expect(map.get("PointBuy")).toBe(PointBuyAbilityScore);
  expect(map.get("Static")).toBe(StaticAbilityScore);
  expect(map.getKey(PointBuyAbilityScore)).toBe("PointBuy");
  expect(map.getKey(StaticAbilityScore)).toBe("Static");
});

test("two-arg @Aspect does not populate aspectRegistry", () => {
  expect(() => getAspectKey(Id)).not.toThrow();
  expect(() => getAspectKey(PointBuyAbilityScore)).toThrow();
});

test("duplicate impl registration throws", () => {
  expect(() => Aspect("PointBuy", BaseAbilityScore)(class {})).toThrow();
});
