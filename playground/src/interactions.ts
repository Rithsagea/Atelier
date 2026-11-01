import { Id } from "@atelier/core/composite/Aspects";
import { createGoblin } from "@atelier/content/monsters/Goblin";
import { Rng } from "@atelier/game/dice/Rng";
import { Game } from "@atelier/game/game/Game";
import { Interactions } from "@atelier/game/game/Interactions";
import { act, autoRoll, fill, listActions } from "@atelier/game/game/Play";
import { Roster } from "@atelier/game/game/Roster";
import { HeroicInspiration } from "@atelier/game/stats/HeroicInspiration";
import { HitPoints } from "@atelier/game/stats/HitPoints";
import { buildFighter } from "./characters";

import "@atelier/content/monsters/index";

// A walk through the interaction model: actions only start an interaction, and anyone fills its
// requirements, by hand or from the game's dice.

const game = new Game();
game.get(Rng).state = 2024;
const fighter = buildFighter();
const goblin = createGoblin();
game.get(Roster).sheets.push(fighter, goblin);
const [fighterId, goblinId] = [fighter.get(Id).value, goblin.get(Id).value];

const hp = (sheet: typeof fighter) => `${sheet.get(HitPoints).current(sheet)} HP`;

/** Print what each open interaction is still waiting for. */
function show() {
  for (const { id, requirements } of game.get(Interactions).list) {
    const open = requirements.filter((r) => r.value === undefined);
    console.log(
      `  ${id} waits on:`,
      open.map((r) =>
        r.kind === "choice"
          ? `${r.key} [${r.options}]`
          : `${r.key} (${r.dice.count}d${r.dice.sides})`,
      ),
    );
  }
}

console.log("actions:", listActions(game, fighterId));

// 1. Attack by hand. Starting the action changes nothing; each fill moves it along.
console.log("\nAttack, filled by hand");
act(game, fighterId, "Attack");
const attack = game.get(Interactions).list[0]!.id;
show();
fill(game, attack, "attack", "0"); // the longsword
fill(game, attack, "target", goblinId);
show();
fill(game, attack, "d20", 14); // a roll from real dice, a DM, or anywhere else
show();
fill(game, attack, "damage", 4); // 4 + 3
console.log(`  goblin: ${hp(goblin)}`);

// 2. Second Wind is a single roll, and its use is spent when it starts.
console.log("\nSecond Wind");
fighter.get(HitPoints).take(fighter, 8);
console.log(`  fighter: ${hp(fighter)}`);
act(game, fighterId, "Second Wind");
autoRoll(game); // fill every open roll from the game's own dice
console.log(`  fighter: ${hp(fighter)}, actions:`, listActions(game, fighterId));

// 3. A long rest needs nothing, so it finishes as it starts. A Human gains Heroic Inspiration,
// which turns the next roll into a roll plus a choice to keep it or reroll.
console.log("\nHeroic Inspiration");
act(game, fighterId, "Long Rest");
console.log(`  inspired: ${fighter.get(HeroicInspiration).value}`);
act(game, fighterId, "Attack");
const inspired = game.get(Interactions).list[0]!.id;
fill(game, inspired, "attack", "0");
fill(game, inspired, "target", goblinId);
fill(game, inspired, "d20", 3);
show(); // the roll is final only once the player keeps or rerolls it
fill(game, inspired, "reroll", "reroll");
show();
autoRoll(game);
console.log(`  inspired: ${fighter.get(HeroicInspiration).value}, goblin: ${hp(goblin)}`);
