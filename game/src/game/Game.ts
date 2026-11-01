import { Id } from "@atelier/core/composite/Aspects";
import { Aspect } from "@atelier/core/composite/Composite";
import { Structure } from "@atelier/core/composite/Structure";
import { Property } from "@atelier/core/serial/Data";
import { Rng } from "../dice/Rng";

@Aspect.Final("GameStatus")
export class GameStatus {
  @Property.Primitive
  value: "active" | "closed" = "active";
}

// The root composite: a memoryless snapshot of the game's current state. History (the event log,
// sequence numbers, the initial seed) lives in the server layer and is never read from here.
// TODO: the roster of characters and monsters, and the encounter.
export const Game = Structure(
  [Id, () => new Id()],
  [GameStatus, () => new GameStatus()],
  [Rng, () => new Rng()],
);
