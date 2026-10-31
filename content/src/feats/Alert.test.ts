import assert from "node:assert/strict";
import { test } from "node:test";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import { Feat } from "@atelier/game/feats/Feat";
import { Feats } from "@atelier/game/sheet/Feats";
import { Sheet, refresh } from "@atelier/game/sheet/Sheet";
import { Human } from "../species/Human";
import { Species } from "@atelier/game/species/Species";
import { BaseAbilityScore, StaticAbilityScore } from "@atelier/game/stats/BaseAbilityScore";
import { Initiative } from "@atelier/game/stats/Initiative";
import { Alert } from "./Alert";
import { feats } from "./index";

function build(withAlert: boolean) {
  const sheet = new Sheet();
  sheet.provide(
    BaseAbilityScore,
    new StaticAbilityScore({
      strength: 15,
      dexterity: 14,
      constitution: 13,
      intelligence: 12,
      wisdom: 10,
      charisma: 8,
    }),
  );
  sheet.provide(Species, new Human());
  if (withAlert) sheet.get(Feats).list.push(new Alert());
  return sheet;
}

function initiativeOf(sheet: InstanceType<typeof Sheet>) {
  refresh(sheet);
  return sheet.get(Initiative).value;
}

test("every listed feat is registered in game's Feat list", () => {
  for (const feat of feats) assert.ok(Feat.typeMap!.inverse().get(feat));
});

test("Alert adds the proficiency bonus to Initiative", () => {
  assert.equal(initiativeOf(build(false)), 2); // DEX 14
  assert.equal(initiativeOf(build(true)), 4);
});

test("a sheet with Alert round-trips through JSON", () => {
  const sheet = build(true);
  const data = JSON.parse(JSON.stringify(serialize(sheet)));
  assert.deepEqual(data.aspects.Feats, { list: [{ $type: "Alert" }] });

  const restored = deserialize(data, Sheet);
  assert.equal(restored.validate(), true);
  assert.equal(initiativeOf(restored), 4);
});
