import { Composite } from "@atelier/core/composite/Composite";
import { Abilities, AbilityScores, AbilityScoresAspect } from "@atelier/core/stats/AbilityScore";
import { PointBuyScores, BaseAbilityScoresAspect } from "@atelier/core/stats/BaseAbilityScore";
import { SheetTemplate } from "@atelier/core/dnd/Sheet";

const sheet = new Composite();
sheet.provide(BaseAbilityScoresAspect, new PointBuyScores());
sheet.provide(AbilityScoresAspect, new AbilityScores());

SheetTemplate.validate(sheet);

const base = sheet.get(BaseAbilityScoresAspect);
const final = sheet.get(AbilityScoresAspect);

for (const ability of Abilities) {
  final.scores[ability] = base.scores[ability];
}

console.log(final.scores);
