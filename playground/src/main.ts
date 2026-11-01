import assert from "node:assert/strict";
import { Id, Name } from "@atelier/core/composite/Aspects";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import { Rng } from "@atelier/game/dice/Rng";
import { Game } from "@atelier/game/game/Game";
import { Sheet, refresh } from "@atelier/game/sheet/Sheet";
import { AbilityScore, abilityModifier } from "@atelier/game/stats/AbilityScore";
import { ArmorClass } from "@atelier/game/stats/ArmorClass";
import { ArmorTraining } from "@atelier/game/stats/ArmorTraining";
import { Attacks } from "@atelier/game/stats/Attacks";
import { PointBuyAbilityScore } from "@atelier/game/stats/BaseAbilityScore";
import { HitDice } from "@atelier/game/stats/HitDice";
import { HitPoints, MaxHitPoints } from "@atelier/game/stats/HitPoints";
import { Initiative } from "@atelier/game/stats/Initiative";
import { Level } from "@atelier/game/stats/Level";
import { SavingThrow } from "@atelier/game/stats/SavingThrow";
import { SkillProficiency } from "@atelier/game/stats/SkillProficiency";
import { SkillScore } from "@atelier/game/stats/SkillScore";
import { WeaponTraining } from "@atelier/game/stats/WeaponTraining";
import { buildFighter } from "./characters";

// Importing a content index is what registers its content into game's lists.
import "@atelier/content/backgrounds/index";
import "@atelier/content/classes/index";
import "@atelier/content/feats/index";
import "@atelier/content/items/index";
import "@atelier/content/species/index";

const sign = (n: number) => (n >= 0 ? `+${n}` : `${n}`);

/** Everything a sheet computes, as plain data, so two sheets can be compared. */
function summarize(sheet: ReturnType<typeof buildFighter>) {
  const hp = sheet.get(HitPoints);
  return {
    name: sheet.get(Name).value,
    level: sheet.get(Level).value,
    proficiencyBonus: sheet.get(Level).proficiencyBonus,
    abilities: sheet.get(AbilityScore).scores,
    skills: Object.fromEntries(
      Object.entries(sheet.get(SkillScore).scores).filter(([, score]) => score !== 0),
    ),
    proficientSkills: [...sheet.get(SkillProficiency).skills].sort(),
    saves: sheet.get(SavingThrow).scores,
    initiative: sheet.get(Initiative).value,
    armorClass: sheet.get(ArmorClass).value,
    armorTraining: [...sheet.get(ArmorTraining).categories].sort(),
    weaponTraining: [...sheet.get(WeaponTraining).categories].sort(),
    hitPoints: {
      current: hp.current(sheet),
      max: sheet.get(MaxHitPoints).value,
      temporary: hp.temporary,
    },
    hitDice: sheet.get(HitDice).remaining(sheet, "d10"),
    attacks: sheet.get(Attacks).list.map((a) => ({
      weapon: a.weapon.constructor.name,
      bonus: a.bonus,
      damage: `${a.damage.count}d${a.damage.sides}${sign(a.damageBonus)} ${a.damageType}`,
    })),
  };
}

const sheet = buildFighter();
sheet.get(HitPoints).take(sheet, 9);
sheet.get(HitPoints).grantTemporary(4);
sheet.get(HitDice).spend(sheet, "d10");
refresh(sheet);
const summary = summarize(sheet);
console.log(
  `${summary.name}, level ${summary.level} Human Fighter (Soldier), id ${sheet.get(Id).value}`,
);
console.log(summary);

// Serialization: the whole sheet survives a trip through JSON and recomputes identically.
const json = JSON.stringify(serialize(sheet));
const restored = deserialize(JSON.parse(json), Sheet);
refresh(restored);
assert.deepEqual(summarize(restored), summary);
console.log(`round-trip ok (${json.length} bytes of JSON)`);

// The Game: seeded dice that keep rolling the same sequence after a restore.
const game = new Game();
game.get(Rng).state = 2024;
const initiativeRoll = game.get(Rng).roll({ count: 1, sides: 20 });
console.log(
  `initiative: d20 ${initiativeRoll} ${sign(summary.initiative)} = ${initiativeRoll + summary.initiative}`,
);
for (const attack of sheet.get(Attacks).list) {
  const toHit = game.get(Rng).roll({ count: 1, sides: 20 }) + attack.bonus;
  const damage = game.get(Rng).roll(attack.damage) + attack.damageBonus;
  console.log(`${attack.weapon.constructor.name}: ${toHit} to hit, ${damage} ${attack.damageType}`);
}
const savedGame = JSON.parse(JSON.stringify(serialize(game)));
const resumed = deserialize(savedGame, Game);
assert.equal(resumed.get(Rng).next(), game.get(Rng).next());
console.log(`game ${game.get(Id).value}: restored dice continue identically`);

// The other BaseAbilityScore variant: point buy, 27 points.
const pointBuy = new PointBuyAbilityScore();
pointBuy.set("strength", 9); // 15
pointBuy.set("dexterity", 7); // 14
pointBuy.set("constitution", 5); // 13
pointBuy.set("intelligence", 4); // 12
pointBuy.set("wisdom", 2); // 10
assert.equal(pointBuy.remaining(), 0);
const modifiers = Object.values(pointBuy.scores).map(abilityModifier).map(sign).join(" ");
console.log(`point buy: ${Object.values(pointBuy.scores).join("/")} (${modifiers}), 0 points left`);
