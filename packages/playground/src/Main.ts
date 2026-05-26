import { Composite } from "@atelier/core/composite/Composite";
import { AbilityScore } from "@atelier/core/stats/AbilityScore";
import { BaseAbilityScore, PointBuyScore } from "@atelier/core/stats/BaseAbilityScore";

const sheet = new Composite();
sheet.provide(BaseAbilityScore, new PointBuyScore());

const score = new AbilityScore();
score.refresh(sheet);

console.log(score.scores);
