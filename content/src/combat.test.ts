import assert from "node:assert/strict";
import { test } from "node:test";
import { Id } from "@atelier/core/composite/Aspects";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import { Background } from "@atelier/game/backgrounds/Background";
import { BackgroundAbilities } from "@atelier/game/backgrounds/BackgroundAbilities";
import { Class } from "@atelier/game/classes/Class";
import { Rng } from "@atelier/game/dice/Rng";
import { Game } from "@atelier/game/game/Game";
import { Interactions } from "@atelier/game/game/Interactions";
import { act, autoRoll, fill, listActions } from "@atelier/game/game/Play";
import { Roster } from "@atelier/game/game/Roster";
import { Inventory } from "@atelier/game/sheet/Inventory";
import { Sheet, refresh } from "@atelier/game/sheet/Sheet";
import { Species } from "@atelier/game/species/Species";
import { Attacks } from "@atelier/game/stats/Attacks";
import { BaseAbilityScore, StaticAbilityScore } from "@atelier/game/stats/BaseAbilityScore";
import { HeroicInspiration } from "@atelier/game/stats/HeroicInspiration";
import { HitDice } from "@atelier/game/stats/HitDice";
import { HitPoints } from "@atelier/game/stats/HitPoints";
import { Level } from "@atelier/game/stats/Level";
import { Resources } from "@atelier/game/stats/Resources";
import { Soldier } from "./backgrounds/Soldier";
import { Champion } from "./classes/Champion";
import { Fighter } from "./classes/Fighter";
import { FighterSubclass } from "./classes/FighterSubclass";
import { ChainMail } from "./items/ChainMail";
import { Longsword } from "./items/Longsword";
import { Shield } from "./items/Shield";
import { Human } from "./species/Human";
import { createGoblin } from "./monsters/Goblin";

import "./backgrounds/index";
import "./classes/index";
import "./items/index";
import "./monsters/index";
import "./species/index";

// The reference character: a Human Fighter (Soldier) on the Standard Array.
function fighter(level = 1, champion = false) {
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
  const soldier = new Soldier();
  soldier.get(BackgroundAbilities).raise("strength", "constitution");
  sheet.provide(Background, soldier);
  const fighter = new Fighter();
  if (champion) fighter.provide(FighterSubclass, new Champion());
  sheet.provide(Class, fighter);
  sheet.get(Level).value = level;
  const inventory = sheet.get(Inventory);
  for (const item of [new ChainMail(), new Shield(), new Longsword()]) {
    inventory.items.push(item);
    inventory.equip(item);
  }
  refresh(sheet);
  return sheet;
}

function setup(seed = 1, fighters = fighter()) {
  const game = new Game();
  game.get(Rng).state = seed;
  const goblin = createGoblin();
  game.get(Roster).sheets.push(fighters, goblin);
  return {
    game,
    hero: fighters,
    goblin,
    heroId: fighters.get(Id).value,
    goblinId: goblin.get(Id).value,
  };
}

const open = (game: InstanceType<typeof Game>) => game.get(Interactions).list;

/** Start an attack by `heroId` on `goblinId`, with the first attack, and fill the choices. */
function attack(game: InstanceType<typeof Game>, heroId: string, goblinId: string) {
  act(game, heroId, "Attack");
  const { id } = open(game)[0]!;
  fill(game, id, "attack", "0");
  fill(game, id, "target", goblinId);
  return id;
}

test("the reference character has the SRD level 1 numbers", () => {
  const { hero } = setup();
  assert.equal(hero.get(HitPoints).current(hero), 12); // 10 + Constitution 14 (+2)
  const [sword] = hero.get(Attacks).list;
  assert.equal(sword!.bonus, 5); // Strength 17 (+3) and proficiency +2
  assert.equal(sword!.damageBonus, 3);
});

test("a Goblin is AC 15 and attacks at +4 for 1d6+2", () => {
  const goblin = createGoblin();
  const [scimitar] = goblin.get(Attacks).list;
  assert.equal(goblin.get(HitPoints).current(goblin), 10);
  assert.deepEqual(
    [scimitar!.bonus, scimitar!.damage, scimitar!.damageBonus],
    [4, { count: 1, sides: 6 }, 2],
  );
});

test("an attack does nothing until its rolls are filled", () => {
  const { game, goblin, goblinId, heroId } = setup();
  assert.deepEqual(listActions(game, heroId), ["Attack", "Short Rest", "Long Rest", "Second Wind"]);

  const id = attack(game, heroId, goblinId);
  assert.equal(goblin.get(HitPoints).damage, 0);
  fill(game, id, "d20", 12); // 12 + 5 = 17 against AC 15
  assert.equal(goblin.get(HitPoints).damage, 0);
  fill(game, id, "damage", 6); // 6 + 3
  assert.equal(goblin.get(HitPoints).damage, 9);
  assert.equal(open(game).length, 0);
});

test("a miss rolls no damage, and a natural 1 misses whatever the bonus", () => {
  const { game, goblin, goblinId, heroId } = setup();
  fill(game, attack(game, heroId, goblinId), "d20", 9); // 14 against AC 15
  assert.equal(open(game).length, 0);

  fill(game, attack(game, heroId, goblinId), "d20", 1);
  assert.equal(open(game).length, 0);
  assert.equal(goblin.get(HitPoints).damage, 0);
});

const damageDice = (game: InstanceType<typeof Game>) => {
  const requirement = open(game)[0]!.requirements[0]!;
  return requirement.kind === "roll" ? requirement.dice : undefined;
};

test("a critical hit doubles the damage dice, and a Champion crits on 19", () => {
  const plain = setup();
  fill(plain.game, attack(plain.game, plain.heroId, plain.goblinId), "d20", 19); // 24 hits
  assert.deepEqual(damageDice(plain.game), { count: 1, sides: 8 });

  const { game, goblinId, heroId } = setup(1, fighter(3, true));
  fill(game, attack(game, heroId, goblinId), "d20", 19);
  assert.deepEqual(damageDice(game), { count: 2, sides: 8 });

  const early = setup(1, fighter(2, true)); // Improved Critical is a level 3 feature
  assert.equal(early.hero.get(Attacks).list[0]!.critical, 20);
});

test("Second Wind heals twice, is refused a third time, and a short rest restores one use", () => {
  const { game, hero, heroId } = setup();
  hero.get(HitPoints).take(hero, 11); // 1 of 12 left

  act(game, heroId, "Second Wind");
  fill(game, open(game)[0]!.id, "heal", 4); // 4 + Fighter level 1
  assert.equal(hero.get(HitPoints).current(hero), 6);

  act(game, heroId, "Second Wind");
  fill(game, open(game)[0]!.id, "heal", 10);
  assert.equal(hero.get(HitPoints).current(hero), 12); // 6 + 11, up to the maximum

  assert.ok(!listActions(game, heroId).includes("Second Wind"));
  assert.throws(() => act(game, heroId, "Second Wind"), /not an action/);

  act(game, heroId, "Short Rest");
  fill(game, open(game)[0]!.id, "spend", "0");
  assert.equal(hero.get(Resources).remaining(hero, "Second Wind"), 1);
  assert.ok(listActions(game, heroId).includes("Second Wind"));
});

test("a short rest heals the Hit Dice spent, with the Constitution modifier for each", () => {
  const { game, hero, heroId } = setup();
  hero.get(HitPoints).take(hero, 9);
  act(game, heroId, "Short Rest");
  const { id } = open(game)[0]!;
  assert.throws(() => fill(game, id, "spend", "2"), /not an option/); // one d10 at level 1
  fill(game, id, "spend", "1");
  fill(game, id, "heal", 3);
  assert.equal(hero.get(HitPoints).current(hero), 8); // 3 + 2, from 3
  assert.equal(hero.get(HitDice).remaining(hero, "d10"), 0);
});

test("a long rest restores everything and a Human gains Heroic Inspiration", () => {
  const { game, hero, heroId } = setup(1, fighter(3));
  hero.get(HitPoints).take(hero, 15);
  hero.get(HitDice).spend(hero, "d10");
  hero.get(HitDice).spend(hero, "d10");
  hero.get(Resources).spend(hero, "Second Wind");
  assert.equal(hero.get(HeroicInspiration).value, false);

  act(game, heroId, "Long Rest"); // needs nothing, so it finishes at once
  assert.equal(open(game).length, 0);
  assert.equal(hero.get(HitPoints).damage, 0);
  assert.equal(hero.get(HitDice).remaining(hero, "d10"), 2); // half of 3, rounded down
  assert.equal(hero.get(Resources).remaining(hero, "Second Wind"), 2);
  assert.equal(hero.get(HeroicInspiration).value, true);
});

// A Human long-rests, then attacks and rerolls the d20. Returns the final snapshot.
function inspiredAttack(seed: number) {
  const { game, goblinId, heroId } = setup(seed);
  act(game, heroId, "Long Rest");
  const id = attack(game, heroId, goblinId);
  autoRoll(game); // the d20
  const pending = open(game)[0]!;
  assert.equal(pending.requirements.at(-1)!.key, "reroll");
  fill(game, id, "reroll", "reroll");
  autoRoll(game); // the new d20, then any damage
  return JSON.stringify([
    game.get(Rng).state,
    game
      .get(Roster)
      .sheets.map((sheet) => [sheet.get(HitPoints).damage, sheet.get(HeroicInspiration).value]),
  ]);
}

test("a reroll is spent once, and replays identically from the same seed", () => {
  const first = inspiredAttack(7);
  assert.equal(first, inspiredAttack(7));
  assert.notEqual(first, inspiredAttack(8)); // a different seed rolls differently
  assert.equal(JSON.parse(first)[1][0][1], false); // the inspiration was spent on the reroll
});

// The fighter swings at the goblin until it drops. Returns the sheet state after each swing.
function fight(game: InstanceType<typeof Game>, heroId: string, goblinId: string) {
  const goblin = game.get(Roster).get(goblinId);
  const log: number[] = [];
  for (let swing = 0; swing < 50 && goblin.get(HitPoints).current(goblin) > 0; swing++) {
    attack(game, heroId, goblinId);
    autoRoll(game);
    log.push(goblin.get(HitPoints).damage);
  }
  return log;
}

test("a scripted Fighter vs Goblin fight is deterministic and survives a restart", () => {
  const a = setup(42);
  const log = fight(a.game, a.heroId, a.goblinId);
  assert.ok(a.goblin.get(HitPoints).current(a.goblin) <= 0);
  const b = setup(42);
  assert.deepEqual(fight(b.game, b.heroId, b.goblinId), log);

  // Stop after the first swing, save, load, and finish: the same fight.
  const c = setup(42);
  attack(c.game, c.heroId, c.goblinId);
  autoRoll(c.game);
  const restored = deserialize(JSON.parse(JSON.stringify(serialize(c.game))), Game);
  for (const sheet of restored.get(Roster).sheets) refresh(sheet);
  assert.deepEqual(fight(restored, c.heroId, c.goblinId).at(-1), log.at(-1));
});

test("a game with an interaction open in the middle round-trips", () => {
  const { game, goblinId, heroId } = setup();
  const id = attack(game, heroId, goblinId);
  fill(game, id, "d20", 15);
  const restored = deserialize(JSON.parse(JSON.stringify(serialize(game))), Game);
  for (const sheet of restored.get(Roster).sheets) refresh(sheet);
  fill(restored, id, "damage", 8);
  assert.equal(restored.get(Roster).get(goblinId).get(HitPoints).damage, 10); // 8 + 2, the Goblin's 10
});
