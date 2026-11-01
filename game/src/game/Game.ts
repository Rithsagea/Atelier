import { Id } from "@atelier/core/composite/Aspects";
import { Structure } from "@atelier/core/composite/Structure";
import { Rng } from "../dice/Rng";

// The root composite: a memoryless snapshot of the game's current state. History (the event log,
// sequence numbers, the initial seed) lives in the server layer and is never read from here.
// TODO: the roster of characters and monsters, and the encounter.
export const Game = Structure([Id, () => new Id()], [Rng, () => new Rng()]);
