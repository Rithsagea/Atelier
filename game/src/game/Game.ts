import { Id } from "@atelier/core/composite/Aspects";
import { Structure } from "@atelier/core/composite/Structure";
import { Rng } from "../dice/Rng";
import { Interactions } from "./Interactions";
import { Roster } from "./Roster";

// The root composite: a memoryless snapshot of the game's current state. History (the event log,
// sequence numbers, the initial seed) lives in the server layer and is never read from here.
// TODO: the encounter, once turns exist.
export const Game = Structure(
  [Id, () => new Id()],
  [Rng, () => new Rng()],
  [Roster, () => new Roster()],
  [Interactions, () => new Interactions()],
);
