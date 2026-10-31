import { AspectHolder, Holder } from "@atelier/core/composite/Aspects";
import { Aspect } from "@atelier/core/composite/Composite";
import { Structure } from "@atelier/core/composite/Structure";
import { Species } from "@atelier/game/species/Species";
import { Skillful } from "./Skillful";

// Versatile (an Origin feat of choice) is not modelled here; the chosen feat lives in the
// sheet's Feats.
@Aspect("Human", Species)
export class Human extends Structure(
  [Holder, () => new AspectHolder()],
  [Skillful, () => new Skillful()],
) {}
