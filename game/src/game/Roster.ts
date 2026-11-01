import { Id } from "@atelier/core/composite/Aspects";
import { Aspect, type Composite } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import { Sheet } from "../sheet/Sheet";

// Every character and monster in the game. Derived stats are transient: refresh after loading.
@Aspect.Final("Roster")
export class Roster {
  @Property.List(Sheet)
  sheets: Composite[] = [];

  get(id: string): Composite {
    const sheet = this.sheets.find((s) => s.get(Id).value === id);
    if (!sheet) throw new Error(`No composite "${id}" in the game`);
    return sheet;
  }
}
