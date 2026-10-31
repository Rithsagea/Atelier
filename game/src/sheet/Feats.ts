import { Aspect, type Composite } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import { Feat } from "../feats/Feat";

@Aspect("Feats")
export class Feats {
  @Property.List(Feat.typeMap)
  list: Composite[] = [];
}
