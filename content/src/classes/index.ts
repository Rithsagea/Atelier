import { Defense } from "./Defense";
import { Fighter } from "./Fighter";

// Importing this module registers every class below into game's `Class` list, and every fighting
// style into content's own `FightingStyle` list.
export const classes = [Fighter];
export const fightingStyles = [Defense];
