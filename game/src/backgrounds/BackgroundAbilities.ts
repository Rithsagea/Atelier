import { Composite, RegistryKey } from "@atelier/core/composite/Composite";
import { Property, type SerializationStrategy } from "@atelier/core/serial/Data";
import { type Ability, type AbilityScore, AbilityScoreContributor } from "../stats/AbilityScore";

/** Heterogeneous: +2 to one ability and +1 to another. Homogeneous: +1 to every ability offered. */
export type AbilityIncreases<A extends Ability> =
  { type: "heterogeneous"; double: A; single: A } | { type: "homogeneous" };

// Distinctness is checked whenever increases cross the serialization boundary, in either
// direction, rather than when they are written.
const distinct: SerializationStrategy<AbilityIncreases<Ability>> = {
  serialize: assertDistinct,
  deserialize: (data) => assertDistinct(data as AbilityIncreases<Ability>),
};

function assertDistinct(increases: AbilityIncreases<Ability>): AbilityIncreases<Ability> {
  if (increases.type === "heterogeneous" && increases.double === increases.single) {
    throw new Error(`Ability increases must name different abilities: ${increases.double}`);
  }
  return increases;
}

// A background lists three abilities; the player raises one by 2 and another by 1, or all
// three by 1. `A` narrows which abilities a background offers. Empty until chosen.
export abstract class AbstractBackgroundAbilities<A extends Ability = Ability> extends Composite {
  /** The three abilities this background offers. */
  abstract readonly abilities: readonly [A, A, A];

  @Property.Serialize(distinct)
  private increases?: AbilityIncreases<A>;

  constructor() {
    super();
    this.provide(AbilityScoreContributor, {
      apply: (score: AbilityScore) => {
        const increases = this.increases;
        if (increases?.type === "heterogeneous") {
          score.scores[increases.double] += 2;
          score.scores[increases.single] += 1;
        } else if (increases?.type === "homogeneous") {
          for (const ability of this.abilities) score.scores[ability] += 1;
        }
      },
    });
  }

  /** +2 to one ability and +1 to another. */
  raise(plusTwo: A, plusOne: A): void {
    this.increases = { type: "heterogeneous", double: plusTwo, single: plusOne };
  }

  /** +1 to each of the three abilities. */
  raiseAll(): void {
    this.increases = { type: "homogeneous" };
  }
}

export const BackgroundAbilities = new RegistryKey<AbstractBackgroundAbilities>(
  "BackgroundAbilities",
);
