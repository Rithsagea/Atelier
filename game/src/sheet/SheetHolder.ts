import { AspectHolder } from "@atelier/core/composite/Aspects";
import { type Composite } from "@atelier/core/composite/Composite";
import { Inventory } from "./Inventory";
import { Feats } from "./Feats";

// The aspect composites (class, species, ...) plus the feats and the equipped items, which are lists
// rather than aspects. Carried items are not visited: only what is in use contributes.
export class SheetHolder extends AspectHolder {
  *children(sheet: Composite): Iterable<Composite> {
    yield* super.children(sheet);
    yield* sheet.get(Feats).list;
    yield* sheet.get(Inventory).equipment;
  }
}
