import { AspectKey, type Composite } from "@atelier/core/composite/Composite";
import { BiMap } from "@atelier/core/util/Algorithms";
import { type Constructor } from "@atelier/core/util/Types";

// The list of known species. Content registers into it with `@Aspect(name, Species)`; it
// exists up front so a sheet serializes its species before any content has loaded.
export const Species = new AspectKey<Composite>("Species");
Species.typeMap = new BiMap<string, Constructor<Composite>>();
