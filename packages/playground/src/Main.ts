import { Sheet } from "@atelier/core/dnd/Sheet";
import { Id } from "@atelier/core/composite/Id";
import { AbilityScore } from "@atelier/core/stats/AbilityScore";
import { BaseAbilityScore, PointBuyAbilityScore } from "@atelier/core/stats/BaseAbilityScore";

const sheet = new Sheet();
sheet.provide(BaseAbilityScore, new PointBuyAbilityScore());

const score = sheet.get(AbilityScore);
score.refresh(sheet);

console.log(sheet.get(Id).value);
console.log(score.scores);
