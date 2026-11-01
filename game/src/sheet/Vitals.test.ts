import assert from "node:assert/strict";
import { test } from "node:test";
import { Aspect, Composite } from "@atelier/core/composite/Composite";
import { deserialize, serialize } from "@atelier/core/serial/Data";
import { Feat } from "../feats/Feat";
import { Armor } from "../equipment/Armor";
import { Item } from "../equipment/Item";
import { ArmorClass, ArmorClassContributor } from "../stats/ArmorClass";
import { BaseAbilityScore, StaticAbilityScore } from "../stats/BaseAbilityScore";
import { HitDice, HitDicePoolContributor } from "../stats/HitDice";
import { HitPoints, MaxHitPointsContributor } from "../stats/HitPoints";
import { SavingThrow, SavingThrowProficiencyContributor } from "../stats/SavingThrow";
import { Inventory } from "./Inventory";
import { Feats } from "./Feats";
import { Sheet, refresh } from "./Sheet";

// Stand in for content, which `game` cannot depend on.
@Aspect.Variant(Item, "TestChain")
class TestChain extends Armor {
  readonly base = 16;
  readonly dexCap = 0;
}

@Aspect.Variant(Item, "TestBreastplate")
class TestBreastplate extends Armor {
  readonly base = 14;
  readonly dexCap = 2;
}

const STANDARD_ARRAY = {
  strength: 15,
  dexterity: 14,
  constitution: 13,
  intelligence: 12,
  wisdom: 10,
  charisma: 8,
};

// Stands in for the Fighter class: a d10 Hit Die per level, 10 + Constitution at level 1.
@Aspect.Variant(Feat, "TestFighter")
class TestFighter extends Composite {
  constructor() {
    super();
    this.provide(MaxHitPointsContributor, { apply: (max) => void (max.value += 11) }); // 10 + CON 13 (+1)
    this.provide(HitDicePoolContributor, { apply: (pool) => void (pool.dice.d10 = 2) });
    this.provide(SavingThrowProficiencyContributor, {
      apply: (proficiency) => void proficiency.abilities.add("strength").add("constitution"),
    });
  }
}

function build() {
  const sheet = new Sheet();
  sheet.provide(BaseAbilityScore, new StaticAbilityScore(STANDARD_ARRAY));
  sheet.get(Feats).list.push(new TestFighter());
  refresh(sheet);
  return sheet;
}

test("HitPoints store damage, so current follows the maximum", () => {
  const sheet = build();
  const hp = sheet.get(HitPoints);
  assert.equal(hp.current(sheet), 11);

  hp.take(sheet, 4);
  assert.equal(hp.current(sheet), 7);

  sheet.get(Feats).list.push(
    (() => {
      const tough = new Composite();
      tough.provide(MaxHitPointsContributor, { apply: (max) => void (max.value += 4) });
      return tough;
    })(),
  );
  refresh(sheet);
  assert.equal(hp.current(sheet), 11);
});

test("damage drains temporary Hit Points first and stops at 0", () => {
  const sheet = build();
  const hp = sheet.get(HitPoints);
  hp.grantTemporary(5);
  hp.take(sheet, 7);
  assert.equal(hp.temporary, 0);
  assert.equal(hp.current(sheet), 9);

  hp.take(sheet, 100);
  assert.equal(hp.current(sheet), 0);
});

test("healing is clamped to the maximum and leaves temporary Hit Points alone", () => {
  const sheet = build();
  const hp = sheet.get(HitPoints);
  hp.take(sheet, 3);
  hp.grantTemporary(2);
  hp.heal(10);
  assert.equal(hp.current(sheet), 11);
  assert.equal(hp.temporary, 2);
});

test("temporary Hit Points keep the larger value instead of stacking", () => {
  const hp = new HitPoints();
  hp.grantTemporary(10);
  hp.grantTemporary(12);
  assert.equal(hp.temporary, 12);
  hp.grantTemporary(3);
  assert.equal(hp.temporary, 12);
});

test("Hit Dice are spent out of the derived pool and refuse to overspend", () => {
  const sheet = build();
  const dice = sheet.get(HitDice);
  assert.equal(dice.remaining(sheet, "d10"), 2);
  dice.spend(sheet, "d10");
  assert.equal(dice.remaining(sheet, "d10"), 1);
  dice.spend(sheet, "d10");
  assert.throws(() => dice.spend(sheet, "d10"), /No d10 Hit Dice/);
  assert.throws(() => dice.spend(sheet, "d8"), /No d8 Hit Dice/);
});

test("HitPoints and HitDice persist through a round trip; the derived parts are rebuilt", () => {
  const sheet = build();
  sheet.get(HitPoints).take(sheet, 4);
  sheet.get(HitPoints).grantTemporary(3);
  sheet.get(HitDice).spend(sheet, "d10");

  const restored = deserialize(JSON.parse(JSON.stringify(serialize(sheet))), Sheet);
  refresh(restored);
  assert.equal(restored.get(HitPoints).current(restored), 7);
  assert.equal(restored.get(HitPoints).temporary, 3);
  assert.equal(restored.get(HitDice).remaining(restored, "d10"), 1);
});

test("saving throws are the ability modifier plus proficiency where granted", () => {
  const saves = build().get(SavingThrow).scores;
  assert.equal(saves.strength, 4); // STR 15 (+2) + proficiency (+2)
  assert.equal(saves.constitution, 3); // CON 13 (+1) + proficiency (+2)
  assert.equal(saves.dexterity, 2); // DEX 14, not proficient
  assert.equal(saves.charisma, -1); // CHA 8
});

test("saving throw proficiency granted twice counts once", () => {
  const sheet = build();
  sheet.get(Feats).list.push(new TestFighter());
  refresh(sheet);
  assert.equal(sheet.get(SavingThrow).scores.strength, 4);
});

test("unarmored Armor Class is 10 plus the Dexterity modifier", () => {
  assert.equal(build().get(ArmorClass).value, 12);
});

test("equipped armor sets the base and caps Dexterity; unequipped armor does nothing", () => {
  const sheet = build();
  const chain = new TestChain();
  const breastplate = new TestBreastplate();
  sheet.get(Inventory).items.push(chain, breastplate);

  refresh(sheet);
  assert.equal(sheet.get(ArmorClass).value, 12);

  sheet.get(Inventory).equip(chain);
  refresh(sheet);
  assert.equal(sheet.get(ArmorClass).value, 16); // heavy armor ignores DEX

  sheet.get(Inventory).unequip(chain);
  sheet.get(Inventory).equip(breastplate);
  refresh(sheet);
  assert.equal(sheet.get(ArmorClass).value, 16); // 14 + DEX 14 (+2, at the cap)
});

test("only one suit of armor can be equipped at a time", () => {
  const sheet = build();
  const [chain, breastplate] = [new TestChain(), new TestBreastplate()];
  sheet.get(Inventory).items.push(chain, breastplate);
  sheet.get(Inventory).equip(chain);
  assert.throws(() => sheet.get(Inventory).equip(breastplate), /armor slot/);
  assert.throws(() => sheet.get(Inventory).equip(new TestChain()), /not in the inventory/);

  sheet.get(Inventory).unequip(chain);
  assert.deepEqual(sheet.get(Inventory).equipment, []);
  assert.deepEqual(sheet.get(Inventory).items, [breastplate, chain]);
  assert.throws(() => sheet.get(Inventory).unequip(chain), /not equipped/);
});

test("a bonus that needs worn armor sees it whatever the contributor order", () => {
  const sheet = build();
  const defense = new Composite(); // stands in for the Defense fighting style
  defense.provide(ArmorClassContributor, { apply: (ac) => void (ac.bonus += ac.armor ? 1 : 0) });
  sheet.get(Feats).list.push(defense); // before the inventory in SheetHolder's order

  const chain = new TestChain();
  sheet.get(Inventory).items.push(chain);
  sheet.get(Inventory).equip(chain);
  refresh(sheet);
  assert.equal(sheet.get(ArmorClass).value, 17);

  sheet.get(Inventory).unequip(chain);
  refresh(sheet);
  assert.equal(sheet.get(ArmorClass).value, 12);
});

test("the inventory and what is equipped round-trip through JSON", () => {
  const sheet = build();
  const chain = new TestChain();
  sheet.get(Inventory).items.push(chain, new TestBreastplate());
  sheet.get(Inventory).equip(chain);

  const data = JSON.parse(JSON.stringify(serialize(sheet)));
  assert.deepEqual(data.aspects.Inventory, {
    items: [{ $type: "TestBreastplate" }],
    equipment: [{ $type: "TestChain" }],
  });

  const restored = deserialize(data, Sheet);
  restored.provide(BaseAbilityScore, new StaticAbilityScore(STANDARD_ARRAY));
  refresh(restored);
  assert.equal(restored.get(ArmorClass).value, 16);
});
