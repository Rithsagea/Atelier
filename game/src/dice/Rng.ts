import { Aspect } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";

/** `count` dice with `sides` faces each: 2d6 is `{ count: 2, sides: 6 }`. */
export interface DiceRoll {
  count: number;
  sides: number;
}

// The game's only source of randomness (mulberry32). Its whole state is one integer, kept in the
// Game snapshot, so a restored game rolls the same dice as one that never stopped.
@Aspect.Final("Rng")
export class Rng {
  @Property.Primitive
  state: number = 0;

  /** Uniform in [0, 1). */
  next(): number {
    this.state = (this.state + 0x6d2b79f5) | 0;
    let t = Math.imul(this.state ^ (this.state >>> 15), 1 | this.state);
    t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t;
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  }

  roll({ count, sides }: DiceRoll): number {
    let total = 0;
    for (let i = 0; i < count; i++) total += 1 + Math.floor(this.next() * sides);
    return total;
  }
}
