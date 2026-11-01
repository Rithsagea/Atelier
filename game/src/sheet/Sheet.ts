import { Holder, Id, Name } from "@atelier/core/composite/Aspects";
import { type Composite } from "@atelier/core/composite/Composite";
import { Structure } from "@atelier/core/composite/Structure";
import { Background } from "../backgrounds/Background";
import { Class } from "../classes/Class";
import { Species } from "../species/Species";
import { Actions } from "../stats/Actions";
import { ArmorTraining } from "../stats/ArmorTraining";
import { Attacks } from "../stats/Attacks";
import { AbilityScore } from "../stats/AbilityScore";
import { BaseAbilityScore } from "../stats/BaseAbilityScore";
import { ArmorClass } from "../stats/ArmorClass";
import { HeroicInspiration } from "../stats/HeroicInspiration";
import { HitDice, HitDicePool } from "../stats/HitDice";
import { HitPoints, MaxHitPoints } from "../stats/HitPoints";
import { Initiative } from "../stats/Initiative";
import { Level } from "../stats/Level";
import { ResourcePool, Resources } from "../stats/Resources";
import { SavingThrow, SavingThrowProficiency } from "../stats/SavingThrow";
import { SkillProficiency } from "../stats/SkillProficiency";
import { SkillScore } from "../stats/SkillScore";
import { WeaponTraining } from "../stats/WeaponTraining";
import { Feats } from "./Feats";
import { Inventory } from "./Inventory";
import { SheetHolder } from "./SheetHolder";

export const Sheet = Structure(
  [Id, () => new Id()],
  [Name, () => new Name()],
  [Level, () => new Level()],
  [Holder, () => new SheetHolder()],
  [BaseAbilityScore],
  [Species],
  [Background],
  [Class],
  [Feats, () => new Feats()],
  [Inventory, () => new Inventory()],
  [HitPoints, () => new HitPoints()],
  [HitDice, () => new HitDice()],
  [HeroicInspiration, () => new HeroicInspiration()],
  [Resources, () => new Resources()],
  [AbilityScore, () => new AbilityScore()],
  [SkillProficiency, () => new SkillProficiency()],
  [SkillScore, () => new SkillScore()],
  [SavingThrowProficiency, () => new SavingThrowProficiency()],
  [SavingThrow, () => new SavingThrow()],
  [Initiative, () => new Initiative()],
  [ArmorTraining, () => new ArmorTraining()],
  [ArmorClass, () => new ArmorClass()],
  [WeaponTraining, () => new WeaponTraining()],
  [Attacks, () => new Attacks()],
  [MaxHitPoints, () => new MaxHitPoints()],
  [HitDicePool, () => new HitDicePool()],
  [ResourcePool, () => new ResourcePool()],
  [Actions, () => new Actions()],
);

/**
 * Recompute every derived stat of a sheet. Each projection is rebuilt from scratch, so calling
 * this twice never double-counts. Order matters: everything reads AbilityScore, SkillScore reads
 * SkillProficiency, SavingThrow reads SavingThrowProficiency, a Shield in ArmorClass reads ArmorTraining, and
 * Attacks reads WeaponTraining, so those come first.
 *
 * TODO: this repeats the same three lines per projection and hard-codes the dependency order.
 * It needs a more careful abstraction (likely a refreshable/computable aspect that declares what
 * it reads) rather than a quick loop; left as is until that is designed.
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

  const armorTraining = new ArmorTraining();
  sheet.provide(ArmorTraining, armorTraining);
  armorTraining.refresh(sheet);

  const armorClass = new ArmorClass();
  sheet.provide(ArmorClass, armorClass);
  armorClass.refresh(sheet);

  const weaponTraining = new WeaponTraining();
  sheet.provide(WeaponTraining, weaponTraining);
  weaponTraining.refresh(sheet);

  const attacks = new Attacks();
  sheet.provide(Attacks, attacks);
  attacks.refresh(sheet);

  const maxHitPoints = new MaxHitPoints();
  sheet.provide(MaxHitPoints, maxHitPoints);
  maxHitPoints.refresh(sheet);

  const hitDicePool = new HitDicePool();
  sheet.provide(HitDicePool, hitDicePool);
  hitDicePool.refresh(sheet);

  const resourcePool = new ResourcePool();
  sheet.provide(ResourcePool, resourcePool);
  resourcePool.refresh(sheet);

  const actions = new Actions();
  sheet.provide(Actions, actions);
  actions.refresh(sheet);
}
