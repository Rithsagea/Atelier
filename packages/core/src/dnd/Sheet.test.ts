import { expect, test } from "bun:test";
import { Composite } from "../composite/Composite";
import { AbilityScore } from "../stats/AbilityScore";
import {
  PointBuyScore,
  StaticAbilityScore,
  BaseAbilityScore,
  POINT_BUY_BUDGET,
} from "../stats/BaseAbilityScore";
import { SheetTemplate } from "./Sheet";

test("validate passes with point buy scores", () => {
  const sheet = new Composite();
  sheet.provide(BaseAbilityScore, new PointBuyScore());
  sheet.provide(AbilityScore, new AbilityScore());
  expect(() => SheetTemplate.validate(sheet)).not.toThrow();
});

test("validate passes with static scores", () => {
  const sheet = new Composite();
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
  sheet.provide(AbilityScore, new AbilityScore());
  expect(() => SheetTemplate.validate(sheet)).not.toThrow();
});

test("validate throws without base ability scores", () => {
  const sheet = new Composite();
  sheet.provide(AbilityScore, new AbilityScore());
  expect(() => SheetTemplate.validate(sheet)).toThrow();
});

test("validate throws without ability scores", () => {
  const sheet = new Composite();
  sheet.provide(BaseAbilityScore, new PointBuyScore());
  expect(() => SheetTemplate.validate(sheet)).toThrow();
});

test("default base scores cost zero points", () => {
  const scores = new PointBuyScore();
  expect(scores.spent()).toBe(0);
  expect(scores.remaining()).toBe(POINT_BUY_BUDGET);
});

test("set base score spends points", () => {
  const scores = new PointBuyScore();
  scores.set("strength", 9);
  expect(scores.spent()).toBe(9);
  expect(scores.remaining()).toBe(POINT_BUY_BUDGET - 9);
});

test("set base score throws when invalid cost", () => {
  const scores = new PointBuyScore();
  expect(() => scores.set("strength", 6)).toThrow();
  expect(() => scores.set("strength", -1)).toThrow();
});

test("set base score throws when over budget", () => {
  const scores = new PointBuyScore();
  scores.set("strength", 9);
  scores.set("dexterity", 9);
  scores.set("constitution", 9);
  expect(() => scores.set("intelligence", 9)).toThrow();
});

test("lowering base score refunds points", () => {
  const scores = new PointBuyScore();
  scores.set("strength", 7);
  scores.set("strength", 2);
  expect(scores.spent()).toBe(2);
});
