import { Holder } from "./Aspects";
import { AspectKey, type Composite } from "./Composite";

/** Anything that can write to a `T`: the mutable state of a computed stat. */
export interface Contributor<T> {
  apply(target: T, owner: Composite): void;
}

export const ContributorKey = <T>(name: string) => new AspectKey<Contributor<T>>(name);

/** Let every child of `owner` that can contribute to `target` write to it. */
export function contribute<T>(key: AspectKey<Contributor<T>>, owner: Composite, target: T): void {
  for (const child of owner.get(Holder).children(owner)) {
    if (child.has(key)) child.get(key).apply(target, owner);
  }
}
