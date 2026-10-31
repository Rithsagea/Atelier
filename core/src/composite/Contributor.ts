import { Holder } from "./Aspects";
import { AspectKey, type Composite } from "./Composite";

/** Anything that can write to a `T`: the mutable state of a computed stat. */
export interface Contributor<T> {
  apply(target: T, owner: Composite): void;
}

export const ContributorKey = <T>(name: string) => new AspectKey<Contributor<T>>(name);

// Children, then their children, through each composite's own Holder.
function* descendants(parent: Composite, holder: Holder): Iterable<Composite> {
  for (const child of holder.children(parent)) {
    yield child;
    const nested = child.suppose(Holder);
    if (nested) yield* descendants(child, nested);
  }
}

/** Let every descendant of `owner` that can contribute to `target` write to it. */
export function contribute<T>(key: AspectKey<Contributor<T>>, owner: Composite, target: T): void {
  for (const child of descendants(owner, owner.get(Holder))) {
    if (child.has(key)) child.get(key).apply(target, owner);
  }
}
