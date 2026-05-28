import { Structure } from "@atelier/core/composite/Structure";
import { AbilityScore } from "@atelier/core/stats/AbilityScore";
import { BaseAbilityScore, PointBuyScore } from "@atelier/core/stats/BaseAbilityScore";

class Id {
  readonly value = crypto.randomUUID();
}

const Entity = Structure([Id, () => new Id()]);

const sheet = new Entity();
const score = new AbilityScore();
sheet.provide(BaseAbilityScore, new PointBuyScore());
sheet.provide(AbilityScore, score);

score.refresh(sheet);

console.log(sheet.get(Id).value);
console.log(score.scores);
