import { Template } from "../composite/Template";
import { AbilityScoresAspect } from "../stats/AbilityScore";
import { BaseAbilityScoresAspect } from "../stats/BaseAbilityScore";

export const SheetTemplate = new Template("Sheet", [BaseAbilityScoresAspect, AbilityScoresAspect]);
