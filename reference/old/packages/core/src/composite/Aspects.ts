import { Aspect, AspectKey, Composite } from "./Composite";
import { Property } from "../serial/Data";

export interface Holder<T extends Composite = Composite> {
  children(parent: T): Iterable<Composite>;
}

export const Holder = new AspectKey<Holder<Composite>>("Holder");

export class AspectHolder<T extends Composite = Composite> implements Holder<T> {
  *children(parent: T): Iterable<Composite> {
    for (const value of parent.aspects.values()) {
      if (value instanceof Composite) yield value as Composite;
    }
  }
}

@Aspect("Id")
export class Id {
  @Property.Primitive
  value: string = crypto.randomUUID();
}
