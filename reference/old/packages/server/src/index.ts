import { Aspect } from "@atelier/core/actor/Aspect";

const HpAspect = new Aspect<number>("HitPoints");
console.log(
  `[OK] Imported Aspect from @atelier/core — name: ${HpAspect.name}, id: ${String(HpAspect.id)}`,
);
