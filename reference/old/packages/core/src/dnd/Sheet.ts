import { AspectHolder, Holder, Id } from "../composite/Aspects";
import { Structure } from "../composite/Structure";
import { AbilityScore } from "../stats/AbilityScore";
import { BaseAbilityScore } from "../stats/BaseAbilityScore";
import { SkillScore } from "../stats/Stats";

export const Sheet = Structure(
  [Id, () => new Id()],
  [Holder, () => new AspectHolder()],
  [BaseAbilityScore],
  [AbilityScore, () => new AbilityScore()],
  [SkillScore, () => new SkillScore()],
);
