import { RegistryKey, type Composite } from "@atelier/core/composite/Composite";

// The list of known backgrounds. Content registers into it with
// `@Aspect.Variant(Background, name)`; it exists up front so a sheet serializes its background
// before any content has loaded.
export const Background = new RegistryKey<Composite>("Background");

// The parts of an SRD background, each a slot that content fills with its own variant. The
// ability increases slot lives in BackgroundAbilities, since its rules are shared.
export const BackgroundFeat = new RegistryKey<Composite>("BackgroundFeat");
export const BackgroundSkills = new RegistryKey<Composite>("BackgroundSkills");
export const BackgroundTool = new RegistryKey<Composite>("BackgroundTool");
export const BackgroundEquipment = new RegistryKey<Composite>("BackgroundEquipment");
