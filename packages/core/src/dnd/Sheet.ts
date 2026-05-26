import { Template } from "../composite/Composite";
import { AbilityScore } from "../stats/AbilityScore";
import { BaseAbilityScore } from "../stats/BaseAbilityScore";

export const SheetTemplate = new Template("Sheet", [BaseAbilityScore, AbilityScore]);
