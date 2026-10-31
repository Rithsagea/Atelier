import { Holder, Id, Name } from "@atelier/core/composite/Aspects";
import { Structure } from "@atelier/core/composite/Structure";
import { Species } from "../species/Species";
import { AbilityScore } from "../stats/AbilityScore";
import { BaseAbilityScore } from "../stats/BaseAbilityScore";
import { Initiative } from "../stats/Initiative";
import { Level } from "../stats/Level";
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
  [Feats, () => new Feats()],
  [AbilityScore, () => new AbilityScore()],
  [SkillScore, () => new SkillScore()],
  [Initiative, () => new Initiative()],
);
