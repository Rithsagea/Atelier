import { Aspect } from "../composite/Aspect";
import { type Ability, Abilities } from "./AbilityScore";

export interface BaseAbilityScores {
  scores: Record<Ability, number>;
}

export const BaseAbilityScoresAspect = new Aspect<BaseAbilityScores>("BaseAbilityScores");

// --- Implementations ---

const POINT_BUY_COSTS: Record<number, number> = {
  8: 0,
  9: 1,
  10: 2,
  11: 3,
  12: 4,
  13: 5,
  14: 7,
  15: 9,
};

export const POINT_BUY_BUDGET = 27;

export class PointBuyScores implements BaseAbilityScores {
  scores: Record<Ability, number> = {
    strength: 8,
    dexterity: 8,
    constitution: 8,
    intelligence: 8,
    wisdom: 8,
    charisma: 8,
  };

  cost(score: number): number {
    return POINT_BUY_COSTS[score] ?? Infinity;
  }

  spent(): number {
    return Abilities.reduce((sum, a) => sum + this.cost(this.scores[a]), 0);
  }

  remaining(): number {
    return POINT_BUY_BUDGET - this.spent();
  }

  set(ability: Ability, score: number): void {
    if (score < 8 || score > 15) throw new Error(`Score ${score} out of point buy range [8, 15]`);
    const delta = this.cost(score) - this.cost(this.scores[ability]);
    if (delta > this.remaining())
      throw new Error(`Not enough points (need ${delta}, have ${this.remaining()})`);
    this.scores[ability] = score;
  }
}

export class StaticAbilityScores implements BaseAbilityScores {
  constructor(readonly scores: Record<Ability, number>) {}
}
