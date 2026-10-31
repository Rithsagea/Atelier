import { RegistryKey, type Composite } from "@atelier/core/composite/Composite";

// The list of known species. Content registers into it with `@Aspect.Variant(Species, name)`; it
// exists up front so a sheet serializes its species before any content has loaded.
export const Species = new RegistryKey<Composite>("Species");
