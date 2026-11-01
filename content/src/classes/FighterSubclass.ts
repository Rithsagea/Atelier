import { RegistryKey, type Composite } from "@atelier/core/composite/Composite";

// The Fighter subclasses a Fighter can pick at level 3, registered like fighting styles.
export const FighterSubclass = new RegistryKey<Composite>("FighterSubclass");
