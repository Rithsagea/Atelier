import { AspectKey, type Composite } from "@atelier/core/composite/Composite";
import { BiMap } from "@atelier/core/util/Algorithms";
import { type Constructor } from "@atelier/core/util/Types";

// The list of known feats. Content registers into it with `@Aspect(name, Feat)`; it exists
// up front so `Feats` can serialize against it before any content has loaded.
export const Feat = new AspectKey<Composite>("Feat");
Feat.typeMap = new BiMap<string, Constructor<Composite>>();
