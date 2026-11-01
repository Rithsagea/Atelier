import { Aspect, RegistryKey, type Composite } from "@atelier/core/composite/Composite";
import { Property } from "@atelier/core/serial/Data";
import type { Pending } from "./Pending";

// Content registers interaction types with `@Aspect.Variant(Interaction, name)`.
export const Interaction = new RegistryKey<Pending>("Interaction");

// Everything started but not yet finished. Ids count up so that replay assigns the same ones.
@Aspect.Final("Interactions")
export class Interactions {
  @Property.List(Interaction.typeMap)
  list: Pending[] = [];

  @Property.Primitive
  next: number = 0;

  start(game: Composite, actor: string, pending: Pending): void {
    pending.id = `i${this.next++}`;
    pending.actor = actor;
    this.list.push(pending);
    pending.begin(game);
    pending.settle(game);
  }

  get(id: string): Pending {
    const pending = this.list.find((p) => p.id === id);
    if (!pending) throw new Error(`No open interaction "${id}"`);
    return pending;
  }

  remove(pending: Pending): void {
    this.list.splice(this.list.indexOf(pending), 1);
  }
}
