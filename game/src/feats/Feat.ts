import { RegistryKey, type Composite } from "@atelier/core/composite/Composite";

// The list of known feats. Content registers into it with `@Aspect.Variant(Feat, name)`; it exists
// up front so `Feats` can serialize against it before any content has loaded.
export const Feat = new RegistryKey<Composite>("Feat");
