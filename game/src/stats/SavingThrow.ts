import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { enumMap } from "@atelier/core/util/Types";
import { type Ability, Abilities } from "./Stats";
import { Level } from "./Level";

// Computed projection over the sheet's SavingThrowContributors: the modifier of each saving
// throw. Transient, like SkillScore.
@Aspect.Final("SavingThrow")
export class SavingThrow extends Composite {
  scores: Record<Ability, number> = enumMap(Abilities, (_) => 0);

  refresh(sheet: Composite): void {
    contribute(SavingThrowContributor, sheet, this);
  }
}

export const SavingThrowContributor = ContributorKey<SavingThrow>("SavingThrowContributor");

// Computed projection over the sheet's SavingThrowProficiencyContributors: which saving throws
// the character is proficient in. Transient, like SkillProficiency. Feeds SavingThrow as a
// contributor that adds the bonus per ability.
@Aspect.Final("SavingThrowProficiency")
export class SavingThrowProficiency extends Composite {
  abilities = new Set<Ability>();

  constructor() {
    super();
    this.provide(SavingThrowContributor, {
      apply: (save: SavingThrow, owner: Composite) => {
        for (const ability of this.abilities) {
          save.scores[ability] += owner.get(Level).proficiencyBonus;
        }
      },
    });
  }

  refresh(sheet: Composite): void {
    contribute(SavingThrowProficiencyContributor, sheet, this);
  }
}

export const SavingThrowProficiencyContributor = ContributorKey<SavingThrowProficiency>(
  "SavingThrowProficiencyContributor",
);
