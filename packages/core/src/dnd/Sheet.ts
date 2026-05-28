import { Structure, Holder } from "../composite/Structure";
import { Id } from "../composite/Id";
import { AspectHolder } from "../composite/AspectHolder";

export const Sheet = Structure(
  [Id, () => new Id()],
  [Holder, () => new AspectHolder()],
);
