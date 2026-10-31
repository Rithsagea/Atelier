import { AspectHolder, Holder } from "@atelier/core/composite/Aspects";
import { Aspect } from "@atelier/core/composite/Composite";
import { Structure } from "@atelier/core/composite/Structure";
import { Species } from "@atelier/game/species/Species";
import { Skillful } from "./Skillful";

// TODO: Resourceful
// TODO: Versatile (an Origin feat of choice). For now the chosen feat is just an entry in the
// sheet's Feats, and nothing requires a Human to have one.
@Aspect.Variant(Species, "Human")
export class Human extends Structure(
  [Holder, () => new AspectHolder()],
  [Skillful, () => new Skillful()],
) {}
