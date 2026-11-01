import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { enumMap } from "@atelier/core/util/Types";
import { type Ability, Abilities } from "./Stats";
import { Level } from "./Level";

@Aspect.Final("SavingThrow")
export class SavingThrow extends Composite {
  scores: Record<Ability, number> = enumMap(Abilities, (_) => 0);

  refresh(sheet: Composite): void {
    contribute(SavingThrowContributor, sheet, this);
  }
}

export const SavingThrowContributor = ContributorKey<SavingThrow>("SavingThrowContributor");

// Which saving throws the character is proficient in.
@Aspect.Final("SavingThrowProficiency")
export class SavingThrowProficiency extends Composite {
  abilities = new Set<Ability>();

  constructor() {
    super();
    this.provide(SavingThrowContributor, {
      apply: (save, owner) => {
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
