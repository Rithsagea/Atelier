import { Aspect } from "./Composite";
import { Property } from "../serial/Data";

@Aspect("Id")
export class Id {
  @Property.Primitive
  value: string = crypto.randomUUID();
}
