import { AspectHolder } from "@atelier/core/composite/Aspects";
import { type Composite } from "@atelier/core/composite/Composite";
import { Equipment } from "./Equipment";
import { Feats } from "./Feats";

// The aspect composites (class, species, ...) plus the feats and the inventory, which are lists
// rather than aspects.
export class SheetHolder extends AspectHolder {
  *children(sheet: Composite): Iterable<Composite> {
    yield* super.children(sheet);
    yield* sheet.get(Feats).list;
    yield* sheet.get(Equipment).items;
  }
}
