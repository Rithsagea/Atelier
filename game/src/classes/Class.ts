import { RegistryKey, type Composite } from "@atelier/core/composite/Composite";

// The list of known classes. Content registers into it with `@Aspect.Variant(Class, name)`; it
// exists up front so a sheet serializes its class before any content has loaded. The sheet's
// `Level` is the level in this class.
export const Class = new RegistryKey<Composite>("Class");
