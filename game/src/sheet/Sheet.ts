import { Holder, Id, Name } from "@atelier/core/composite/Aspects";
import { type Composite } from "@atelier/core/composite/Composite";
import { Structure } from "@atelier/core/composite/Structure";
import { Background } from "../backgrounds/Background";
import { Species } from "../species/Species";
import { AbilityScore } from "../stats/AbilityScore";
import { BaseAbilityScore } from "../stats/BaseAbilityScore";
import { ArmorClass } from "../stats/ArmorClass";
import { HitDice, HitDicePool } from "../stats/HitDice";
import { HitPoints, MaxHitPoints } from "../stats/HitPoints";
import { Initiative } from "../stats/Initiative";
import { Level } from "../stats/Level";
import { SavingThrow, SavingThrowProficiency } from "../stats/SavingThrow";
import { SkillProficiency } from "../stats/SkillProficiency";
import { SkillScore } from "../stats/SkillScore";
import { Equipment } from "./Equipment";
import { Feats } from "./Feats";
import { SheetHolder } from "./SheetHolder";

export const Sheet = Structure(
  [Id, () => new Id()],
  [Name, () => new Name()],
  [Level, () => new Level()],
  [Holder, () => new SheetHolder()],
  [BaseAbilityScore],
  [Species],
  [Background],
  [Feats, () => new Feats()],
  [Equipment, () => new Equipment()],
  [HitPoints, () => new HitPoints()],
  [HitDice, () => new HitDice()],
  [AbilityScore, () => new AbilityScore()],
  [SkillProficiency, () => new SkillProficiency()],
  [SkillScore, () => new SkillScore()],
  [SavingThrowProficiency, () => new SavingThrowProficiency()],
  [SavingThrow, () => new SavingThrow()],
  [Initiative, () => new Initiative()],
  [ArmorClass, () => new ArmorClass()],
  [MaxHitPoints, () => new MaxHitPoints()],
  [HitDicePool, () => new HitDicePool()],
);

/**
 * Recompute every derived stat of a sheet. Each projection is rebuilt from scratch, so calling
 * this twice never double-counts. Order matters: everything reads AbilityScore, SkillScore reads
 * SkillProficiency, and SavingThrow reads SavingThrowProficiency, so those come first.
 */
export function refresh(sheet: Composite): void {
  const abilities = new AbilityScore();
  sheet.provide(AbilityScore, abilities);
  abilities.refresh(sheet);

  const proficiency = new SkillProficiency();
  sheet.provide(SkillProficiency, proficiency);
  proficiency.refresh(sheet);

  const skills = new SkillScore();
  sheet.provide(SkillScore, skills);
  skills.refresh(sheet);

  const initiative = new Initiative();
  sheet.provide(Initiative, initiative);
  initiative.refresh(sheet);

  const proficientSaves = new SavingThrowProficiency();
  sheet.provide(SavingThrowProficiency, proficientSaves);
  proficientSaves.refresh(sheet);

  const saves = new SavingThrow();
  sheet.provide(SavingThrow, saves);
  saves.refresh(sheet);

  const armorClass = new ArmorClass();
  sheet.provide(ArmorClass, armorClass);
  armorClass.refresh(sheet);

  const maxHitPoints = new MaxHitPoints();
  sheet.provide(MaxHitPoints, maxHitPoints);
  maxHitPoints.refresh(sheet);

  const hitDicePool = new HitDicePool();
  sheet.provide(HitDicePool, hitDicePool);
  hitDicePool.refresh(sheet);
}
