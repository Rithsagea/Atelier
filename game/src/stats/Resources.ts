import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { contribute, ContributorKey } from "@atelier/core/composite/Contributor";
import { Property } from "@atelier/core/serial/Data";

// `onShortRest` is how many expended uses a short rest regains; a long rest regains all of them.
export interface ResourceDefinition {
  max: number;
  onShortRest: number;
}

@Aspect.Final("ResourcePool")
export class ResourcePool extends Composite {
  pools: Record<string, ResourceDefinition> = {};

  refresh(sheet: Composite): void {
    contribute(ResourceContributor, sheet, this);
  }
}

export const ResourceContributor = ContributorKey<ResourcePool>("ResourceContributor");

// Uses expended per named resource. The maximum is derived, as with Hit Points.
@Aspect.Final("Resources")
export class Resources {
  @Property.Primitive
  spent: Record<string, number> = {};

  remaining(sheet: Composite, name: string): number {
    return (sheet.get(ResourcePool).pools[name]?.max ?? 0) - (this.spent[name] ?? 0);
  }

  spend(sheet: Composite, name: string): void {
    if (this.remaining(sheet, name) < 1) throw new Error(`No uses of ${name} left`);
    this.spent[name] = (this.spent[name] ?? 0) + 1;
  }

  shortRest(sheet: Composite): void {
    for (const [name, { onShortRest }] of Object.entries(sheet.get(ResourcePool).pools)) {
      this.spent[name] = Math.max(0, (this.spent[name] ?? 0) - onShortRest);
    }
  }

  longRest(): void {
    this.spent = {};
  }
}
