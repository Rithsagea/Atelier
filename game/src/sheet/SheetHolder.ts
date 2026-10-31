import { AspectHolder } from "@atelier/core/composite/Aspects";
import { type Composite } from "@atelier/core/composite/Composite";
import { Feats } from "./Feats";

// The aspect composites (class, species, ...) plus the feats list, which is not an aspect.
export class SheetHolder extends AspectHolder {
  *children(sheet: Composite): Iterable<Composite> {
    yield* super.children(sheet);
    yield* sheet.get(Feats).list;
  }
}
