import { Template } from "../composite/Template";
import { AbilityScoreAspect } from "../stats/AbilityScore";
import { BaseAbilityScoreAspect } from "../stats/BaseAbilityScore";

export const SheetTemplate = new Template("Sheet", [BaseAbilityScoreAspect, AbilityScoreAspect]);
