import { RegistryKey, type Composite } from "@atelier/core/composite/Composite";

// The fighting styles a Fighter can pick. Content registers into it with
// `@Aspect.Variant(FightingStyle, name)`. It lives here, not in game, because only content uses it.
export const FightingStyle = new RegistryKey<Composite>("FightingStyle");
