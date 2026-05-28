import { Sheet } from "@atelier/core/dnd/Sheet";
import { Id } from "@atelier/core/composite/Id";
import { AbilityScore } from "@atelier/core/stats/AbilityScore";
import { BaseAbilityScore, PointBuyScore } from "@atelier/core/stats/BaseAbilityScore";

const sheet = new Sheet();
const score = new AbilityScore();
sheet.provide(BaseAbilityScore, new PointBuyScore());
sheet.provide(AbilityScore, score);

score.refresh(sheet);

console.log(sheet.get(Id).value);
console.log(score.scores);
