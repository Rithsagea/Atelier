package com.tempera.atelier.dnd.types.character.classes;

import com.rithsagea.util.DataUtil;
import com.rithsagea.util.choice.Choice;
import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.character.CharacterClass;
import com.tempera.atelier.dnd.types.character.features.HitPointFeature;
import com.tempera.atelier.dnd.types.character.features.ProficiencyFeature;
import com.tempera.atelier.dnd.types.character.features.StartingEquipmentFeature;
import com.tempera.atelier.dnd.types.character.features.StartingEquipmentFeature.EquipmentOption;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.enums.Skill;
import com.tempera.atelier.dnd.types.equipment.adventuring.PHBQuiver;
import com.tempera.atelier.dnd.types.equipment.ammunition.PHBArrow;
import com.tempera.atelier.dnd.types.equipment.armor.PHBLeatherArmor;
import com.tempera.atelier.dnd.types.equipment.tools.PHBThievesTools;
import com.tempera.atelier.dnd.types.equipment.weapons.PHBDagger;
import com.tempera.atelier.dnd.types.equipment.weapons.PHBRapier;
import com.tempera.atelier.dnd.types.equipment.weapons.PHBShortbow;
import com.tempera.atelier.dnd.types.equipment.weapons.PHBShortsword;

@IndexedItem("rogue")
public class Rogue extends CharacterClass {

	public Rogue() {
		this(false);
	}

	public Rogue(boolean multiclass) {
		super("rogue", "Rogue");

		if (multiclass) {

		} else {
			addAttribute("proficiency", new ProficiencyFeature(
				new Choice<Skill>(4, Skill.ACROBATICS, Skill.ATHLETICS,
					Skill.DECEPTION, Skill.INSIGHT, Skill.INTIMIDATION,
					Skill.INVESTIGATION, Skill.PERCEPTION, Skill.PERFORMANCE,
					Skill.PERSUASION, Skill.SLEIGHT_OF_HAND, Skill.STEALTH),

				EquipmentType.LIGHT_ARMOR, EquipmentType.SIMPLE_WEAPON,
				EquipmentType.HAND_CROSSBOW, EquipmentType.LONGSWORD,
				EquipmentType.RAPIER, EquipmentType.SHORTSWORD,
				EquipmentType.THIEVES_TOOLS,

				Ability.DEXTERITY, Ability.INTELLIGENCE));

			addAttribute("hit-points", new HitPointFeature(8));

			addAttribute("starting-equipment",
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
