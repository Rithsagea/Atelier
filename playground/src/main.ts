import assert from "node:assert/strict";
import { Id, Name } from "@atelier/core/composite/Aspects";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import { Defense } from "@atelier/content/classes/Defense";
import { Fighter } from "@atelier/content/classes/Fighter";
import { FighterSkills } from "@atelier/content/classes/FighterSkills";
import { FightingStyle } from "@atelier/content/classes/FightingStyle";
import { Alert } from "@atelier/content/feats/Alert";
import { ChainMail } from "@atelier/content/items/ChainMail";
import { Longsword } from "@atelier/content/items/Longsword";
import { Shield } from "@atelier/content/items/Shield";
import { Shortbow } from "@atelier/content/items/Shortbow";
import { Human } from "@atelier/content/species/Human";
import { Skillful } from "@atelier/content/species/Skillful";
import { Soldier, SoldierEquipment, SoldierTool } from "@atelier/content/backgrounds/Soldier";
import {
  Background,
  BackgroundEquipment,
  BackgroundTool,
} from "@atelier/game/backgrounds/Background";
import { BackgroundAbilities } from "@atelier/game/backgrounds/BackgroundAbilities";
import { Class } from "@atelier/game/classes/Class";
import { Rng } from "@atelier/game/dice/Rng";
import { Game, GameStatus } from "@atelier/game/game/Game";
import { Feats } from "@atelier/game/sheet/Feats";
import { Inventory } from "@atelier/game/sheet/Inventory";
import { Sheet, refresh } from "@atelier/game/sheet/Sheet";
import { Species } from "@atelier/game/species/Species";
import { AbilityScore, abilityModifier } from "@atelier/game/stats/AbilityScore";
import { ArmorClass } from "@atelier/game/stats/ArmorClass";
import { ArmorTraining } from "@atelier/game/stats/ArmorTraining";
import { Attacks } from "@atelier/game/stats/Attacks";
import {
  BaseAbilityScore,
  PointBuyAbilityScore,
  StaticAbilityScore,
} from "@atelier/game/stats/BaseAbilityScore";
import { HitDice } from "@atelier/game/stats/HitDice";
import { HitPoints, MaxHitPoints } from "@atelier/game/stats/HitPoints";
import { Initiative } from "@atelier/game/stats/Initiative";
import { Level } from "@atelier/game/stats/Level";
import { SavingThrow } from "@atelier/game/stats/SavingThrow";
import { SkillProficiency } from "@atelier/game/stats/SkillProficiency";
import { SkillScore } from "@atelier/game/stats/SkillScore";
import { WeaponTraining } from "@atelier/game/stats/WeaponTraining";

// Importing a content index is what registers its content into game's lists.
import "@atelier/content/backgrounds/index";
import "@atelier/content/classes/index";
import "@atelier/content/feats/index";
import "@atelier/content/items/index";
import "@atelier/content/species/index";

const sign = (n: number) => (n >= 0 ? `+${n}` : `${n}`);

/** Build the character from a blank Sheet, one choice at a time. */
function buildFighter() {
  const sheet = new Sheet();
  sheet.get(Name).value = "Brienne";
  sheet.get(Level).value = 3;

  // Standard Array. Swapping this for PointBuyAbilityScore changes nothing else.
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

  // Species: Human, with its Skillful skill chosen.
  const human = new Human();
  human.get(Skillful).skill = "perception";
  sheet.provide(Species, human);

  // Background: Soldier. Ability increases, gaming set, and equipment package.
  const soldier = new Soldier();
  soldier.get(BackgroundAbilities).raise("strength", "constitution");
  (soldier.get(BackgroundTool) as SoldierTool).gamingSet = "Dice Set";
  (soldier.get(BackgroundEquipment) as SoldierEquipment).choice = "A";
  sheet.provide(Background, soldier);

  // Class: Fighter, with two skills and the Defense fighting style.
  const fighter = new Fighter();
  fighter.get(FighterSkills).choose("athletics", "intimidation");
  fighter.provide(FightingStyle, new Defense());
  sheet.provide(Class, fighter);

  // Origin feat of choice (Human Versatile).
  sheet.get(Feats).list.push(new Alert());

  // Inventory: carry everything, then equip by moving items into `equipment`.
  const inventory = sheet.get(Inventory);
  const [chain, shield, sword, bow] = [
    new ChainMail(),
    new Shield(),
    new Longsword(),
    new Shortbow(),
  ];
  inventory.items.push(chain, shield, sword, bow);
  for (const item of [chain, shield, sword]) inventory.equip(item); // the bow stays in the pack

  // Wounds and spent Hit Dice.
  refresh(sheet);
  sheet.get(HitPoints).take(sheet, 9);
  sheet.get(HitPoints).grantTemporary(4);
  sheet.get(HitDice).spend(sheet, "d10");
  return sheet;
}

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
game.get(GameStatus).value = "closed";
console.log(
  `game ${game.get(Id).value} ${game.get(GameStatus).value}; restored dice continue identically`,
);

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
