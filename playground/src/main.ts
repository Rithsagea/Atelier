import { serialize } from "@atelier/core/serial/Data";
import { Sheet } from "@atelier/game/sheet/Sheet";

console.log(JSON.stringify(serialize(new Sheet())));
