import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { abilityModifier, AbilityScore } from "./AbilityScore";

// Computed projection of Armor Class. Transient, like Initiative. Built in two passes so that
// content never depends on contributor order: worn armor first sets `base`, `dexCap` and `armor`
// (the base contributors), then everything else adds to `bonus` and may read what the armor set
// (the contributors: a shield, the Defense fighting style, a magic item).
@Aspect.Final("ArmorClass")
export class ArmorClass extends Composite {
  base = 10;
  dexCap = Infinity;
  armor = false;
  bonus = 0;
  value = 0;

  refresh(sheet: Composite): void {
    contribute(ArmorClassBaseContributor, sheet, this);
    contribute(ArmorClassContributor, sheet, this);
    const dexterity = abilityModifier(sheet.get(AbilityScore).scores.dexterity);
    this.value = this.base + Math.min(dexterity, this.dexCap) + this.bonus;
  }
}

export const ArmorClassBaseContributor = ContributorKey<ArmorClass>("ArmorClassBaseContributor");
export const ArmorClassContributor = ContributorKey<ArmorClass>("ArmorClassContributor");
