import { Composite } from "./Composite";
import { type Holder } from "./Structure";

export class AspectHolder<T extends Composite = Composite> implements Holder<T> {
  *children(parent: T): Iterable<Composite> {
    for (const value of parent.aspects.values()) {
      if (value instanceof Composite) yield value as Composite;
    }
  }
}
