import { Aspect } from "@atelier/core/composite/Composite";
import { ContributorKey } from "@atelier/core/composite/Contributor";
import { Property } from "@atelier/core/serial/Data";

@Aspect.Final("HeroicInspiration")
export class HeroicInspiration {
  @Property.Primitive
  value: boolean = false;
}

// What a finished long rest grants beyond Hit Points and resources (Human Resourceful).
export const LongRestContributor = ContributorKey<HeroicInspiration>("LongRestContributor");
