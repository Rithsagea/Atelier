package com.atelier.dnd.types.character.classes;

import com.atelier.database.IndexedItem;
import com.atelier.dnd.types.character.CharacterClass;
import com.atelier.dnd.types.character.classes.features.HitPointFeature;
import com.atelier.dnd.types.character.classes.features.ProficiencyFeature;
import com.atelier.dnd.types.character.classes.features.StartingEquipmentFeature;
import com.atelier.dnd.types.character.classes.features.StartingEquipmentFeature.EquipmentOption;
import com.atelier.dnd.types.enums.Ability;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Skill;
import com.atelier.dnd.types.equipment.adventuring.PHBQuiver;
import com.atelier.dnd.types.equipment.ammunition.PHBArrow;
import com.atelier.dnd.types.equipment.armor.PHBLeatherArmor;
import com.atelier.dnd.types.equipment.tools.PHBThievesTools;
import com.atelier.dnd.types.equipment.weapons.PHBDagger;
import com.atelier.dnd.types.equipment.weapons.PHBRapier;
import com.atelier.dnd.types.equipment.weapons.PHBShortbow;
import com.atelier.dnd.types.equipment.weapons.PHBShortsword;
import com.rithsagea.util.DataUtil;
import com.rithsagea.util.choice.Choice;

@IndexedItem("rogue")
public class Rogue extends CharacterClass {

	public Rogue() {
		this(false);
	}

	public Rogue(boolean multiclass) {
		super("rogue");

		if (multiclass) {

		} else {
			addFeature("proficiency", new ProficiencyFeature(
				new Choice<Skill>(4, Skill.ACROBATICS, Skill.ATHLETICS,
					Skill.DECEPTION, Skill.INSIGHT, Skill.INTIMIDATION,
					Skill.INVESTIGATION, Skill.PERCEPTION, Skill.PERFORMANCE,
					Skill.PERSUASION, Skill.SLEIGHT_OF_HAND, Skill.STEALTH),

				EquipmentType.LIGHT_ARMOR, EquipmentType.SIMPLE_WEAPON,
				EquipmentType.HAND_CROSSBOW, EquipmentType.LONGSWORD,
				EquipmentType.RAPIER, EquipmentType.SHORTSWORD,
				EquipmentType.THIEVES_TOOLS,

				Ability.DEXTERITY, Ability.INTELLIGENCE));

			addFeature("hit-points", new HitPointFeature(8));

			addFeature("starting-equipment",
				new StartingEquipmentFeature(
					DataUtil.list(
						new EquipmentOption("a rapier", new PHBRapier()),
						new EquipmentOption("a shortsword",
							new PHBShortsword())),
					DataUtil.list(
						new EquipmentOption("a shortbow and quiver of 20 arrows",
							new PHBShortbow(), new PHBQuiver(), new PHBArrow(20)),
						new EquipmentOption("a shortsword",
							new PHBShortsword())),
					DataUtil.list(
						new EquipmentOption("a burglar's pack"),
						new EquipmentOption("a dungeoneer's pack"),
						new EquipmentOption("an explorer's pack")),
					DataUtil.list(
						new EquipmentOption("Leather armor, two daggers, and thieves' tools",
							new PHBLeatherArmor(), new PHBDagger(2), new PHBThievesTools()))));
		}
	}
}
