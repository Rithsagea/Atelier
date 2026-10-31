import { Holder, Id, Name } from "@atelier/core/composite/Aspects";
import { type Composite } from "@atelier/core/composite/Composite";
import { Structure } from "@atelier/core/composite/Structure";
import { Background } from "../backgrounds/Background";
import { Species } from "../species/Species";
import { AbilityScore } from "../stats/AbilityScore";
import { BaseAbilityScore } from "../stats/BaseAbilityScore";
import { Initiative } from "../stats/Initiative";
import { Level } from "../stats/Level";
import { SkillProficiency } from "../stats/SkillProficiency";
import { SkillScore } from "../stats/Stats";
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
  [AbilityScore, () => new AbilityScore()],
  [SkillProficiency, () => new SkillProficiency()],
  [SkillScore, () => new SkillScore()],
  [Initiative, () => new Initiative()],
);

/**
 * Recompute every derived stat of a sheet. Each projection is rebuilt from scratch, so calling
 * this twice never double-counts. Order matters: SkillScore and Initiative read AbilityScore,
 * and SkillScore reads SkillProficiency, so those come first.
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
}
