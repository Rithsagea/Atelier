import { expect, test } from "bun:test";
import { Composite } from "../composite/Composite";
import { AbilityScores, AbilityScoresAspect } from "../stats/AbilityScore";
import {
  PointBuyScores,
  StaticAbilityScores,
  BaseAbilityScoresAspect,
  POINT_BUY_BUDGET,
} from "../stats/BaseAbilityScore";
import { SheetTemplate } from "./Sheet";

test("validate passes with point buy scores", () => {
  const sheet = new Composite();
  sheet.provide(BaseAbilityScoresAspect, new PointBuyScores());
  sheet.provide(AbilityScoresAspect, new AbilityScores());
  expect(() => SheetTemplate.validate(sheet)).not.toThrow();
});

test("validate passes with static scores", () => {
  const sheet = new Composite();
  sheet.provide(
    BaseAbilityScoresAspect,
    new StaticAbilityScores({
      strength: 15,
      dexterity: 14,
      constitution: 13,
      intelligence: 12,
      wisdom: 10,
      charisma: 8,
    }),
  );
  sheet.provide(AbilityScoresAspect, new AbilityScores());
  expect(() => SheetTemplate.validate(sheet)).not.toThrow();
});

test("validate throws without base ability scores", () => {
  const sheet = new Composite();
  sheet.provide(AbilityScoresAspect, new AbilityScores());
  expect(() => SheetTemplate.validate(sheet)).toThrow();
});

test("validate throws without ability scores", () => {
  const sheet = new Composite();
  sheet.provide(BaseAbilityScoresAspect, new PointBuyScores());
  expect(() => SheetTemplate.validate(sheet)).toThrow();
});

test("default base scores cost zero points", () => {
  const scores = new PointBuyScores();
  expect(scores.spent()).toBe(0);
  expect(scores.remaining()).toBe(POINT_BUY_BUDGET);
});

test("set base score spends points", () => {
  const scores = new PointBuyScores();
  scores.set("strength", 15); // costs 9
  expect(scores.spent()).toBe(9);
  expect(scores.remaining()).toBe(POINT_BUY_BUDGET - 9);
});

test("set base score throws when out of range", () => {
  const scores = new PointBuyScores();
  expect(() => scores.set("strength", 7)).toThrow();
  expect(() => scores.set("strength", 16)).toThrow();
});

test("set base score throws when over budget", () => {
  const scores = new PointBuyScores();
  scores.set("strength", 15); // 9
  scores.set("dexterity", 15); // 9
  scores.set("constitution", 15); // 9 — 27 spent
  expect(() => scores.set("intelligence", 9)).toThrow();
});

test("lowering base score refunds points", () => {
  const scores = new PointBuyScores();
  scores.set("strength", 14); // costs 7
  scores.set("strength", 10); // costs 2, refunds 5
  expect(scores.spent()).toBe(2);
});
