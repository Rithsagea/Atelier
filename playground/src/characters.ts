import { Name } from "@atelier/core/composite/Aspects";
import { Soldier, SoldierEquipment, SoldierTool } from "@atelier/content/backgrounds/Soldier";
import { Champion } from "@atelier/content/classes/Champion";
import { Defense } from "@atelier/content/classes/Defense";
import { Fighter } from "@atelier/content/classes/Fighter";
import { FighterSkills } from "@atelier/content/classes/FighterSkills";
import { FighterSubclass } from "@atelier/content/classes/FighterSubclass";
import { FightingStyle } from "@atelier/content/classes/FightingStyle";
import { Alert } from "@atelier/content/feats/Alert";
import { ChainMail } from "@atelier/content/items/ChainMail";
import { Longsword } from "@atelier/content/items/Longsword";
import { Shield } from "@atelier/content/items/Shield";
import { Shortbow } from "@atelier/content/items/Shortbow";
import { Human } from "@atelier/content/species/Human";
import { Skillful } from "@atelier/content/species/Skillful";
import {
  Background,
  BackgroundEquipment,
  BackgroundTool,
} from "@atelier/game/backgrounds/Background";
import { BackgroundAbilities } from "@atelier/game/backgrounds/BackgroundAbilities";
import { Class } from "@atelier/game/classes/Class";
import { Feats } from "@atelier/game/sheet/Feats";
import { Inventory } from "@atelier/game/sheet/Inventory";
import { Sheet, refresh } from "@atelier/game/sheet/Sheet";
import { Species } from "@atelier/game/species/Species";
import { BaseAbilityScore, StaticAbilityScore } from "@atelier/game/stats/BaseAbilityScore";
import { Level } from "@atelier/game/stats/Level";

// Importing a content index is what registers its content into game's lists.
import "@atelier/content/backgrounds/index";
import "@atelier/content/classes/index";
import "@atelier/content/feats/index";
import "@atelier/content/items/index";
import "@atelier/content/species/index";

/** Build the character from a blank Sheet, one choice at a time. */
export function buildFighter() {
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
  fighter.provide(FighterSubclass, new Champion());
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

  refresh(sheet);
  return sheet;
}
