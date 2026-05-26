import { Aspect } from "../composite/Aspect";
import { enumMap } from "../util/Types";
import { type Ability, Abilities } from "./AbilityScore";

export interface BaseAbilityScore {
  scores: Record<Ability, number>;
}

export const BaseAbilityScoreAspect = new Aspect<BaseAbilityScore>("BaseAbilityScores");

// --- Implementations ---

// points spent → stat value
const POINT_BUY_SCORES: Record<number, number> = {
  0: 8,
  1: 9,
  2: 10,
  3: 11,
  4: 12,
  5: 13,
  7: 14,
  9: 15,
};

export const POINT_BUY_BUDGET = 27;

export class PointBuyScore implements BaseAbilityScore {
  points: Record<Ability, number> = enumMap(Abilities, (_) => 0);

  get scores(): Record<Ability, number> {
    return enumMap(Abilities, (a) => POINT_BUY_SCORES[this.points[a]]);
  }

  spent(): number {
    return Abilities.reduce((sum, a) => sum + this.points[a], 0);
  }

  remaining(): number {
    return POINT_BUY_BUDGET - this.spent();
  }

  set(ability: Ability, cost: number): void {
    if (!(cost in POINT_BUY_SCORES)) throw new Error(`${cost} not a valid point buy cost`);
    const delta = cost - this.points[ability];
    if (delta > this.remaining())
      throw new Error(`Not enough points (need ${delta}, have ${this.remaining()})`);
    this.points[ability] = cost;
  }
}

export class StaticAbilityScore implements BaseAbilityScore {
  constructor(readonly scores: Record<Ability, number>) {}
}
