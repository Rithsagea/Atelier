import { Champion } from "./Champion";
import { Defense } from "./Defense";
import { Fighter } from "./Fighter";
import { SecondWindInteraction } from "./SecondWind";

// Importing this module registers every class below into game's `Class` list, every interaction
// into its `Interaction` list, and every fighting style and subclass into content's own lists.
export const classes = [Fighter];
export const interactions = [SecondWindInteraction];
export const fightingStyles = [Defense];
export const fighterSubclasses = [Champion];
