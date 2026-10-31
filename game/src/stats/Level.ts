import { Aspect } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";

export function proficiencyBonus(level: number): number {
  return 2 + Math.floor((level - 1) / 4);
}

@Aspect.Final("Level")
export class Level {
  @Property.Primitive
  value: number = 1;

  get proficiencyBonus(): number {
    return proficiencyBonus(this.value);
  }
}
