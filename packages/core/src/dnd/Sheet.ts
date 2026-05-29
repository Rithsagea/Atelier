import { AspectHolder, Holder, Id } from "../composite/Aspects";
import { Structure } from "../composite/Structure";
import { AbilityScore, ComputedAbilityScore } from "../stats/AbilityScore";
import { BaseAbilityScore } from "../stats/BaseAbilityScore";
import { ComputedSkillScore, SkillScore } from "../stats/Stats";

export const Sheet = Structure(
  [Id, () => new Id()],
  [Holder, () => new AspectHolder()],
  [BaseAbilityScore],
  [AbilityScore, () => new ComputedAbilityScore()],
  [SkillScore, () => new ComputedSkillScore()],
);
