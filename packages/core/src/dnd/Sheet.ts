import { AspectHolder, Holder, Id } from "../composite/Aspects";
import { Structure } from "../composite/Structure";
import { BaseAbilityScore } from "../stats/BaseAbilityScore";

export const Sheet = Structure(
  [Id, () => new Id()],
  [Holder, () => new AspectHolder()],
  [BaseAbilityScore],
);
