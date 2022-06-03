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
import com.tempera.atelier.dnd.types.equipment.ammunition.PHBArrow;
import com.tempera.atelier.dnd.types.equipment.armor.PHBLeatherArmor;
import com.tempera.atelier.dnd.types.equipment.tools.PHBThievesTools;
import com.tempera.atelier.dnd.types.equipment.weapons.PHBDagger;

@IndexedItem("fighter")
public class Fighter extends CharacterClass{

	public Fighter() {
		this(false);
	}
	
	public Fighter(boolean multiclass) {
		super("fighter", "Fighter");

		if (multiclass) {

		} else {
			addAttribute("proficiency", new ProficiencyFeature(
				new Choice<Skill>(2, Skill.ACROBATICS, Skill.ANIMAL_HANDLING,
					Skill.ATHLETICS, Skill.HISTORY, Skill.INSIGHT, 
					Skill.INTIMIDATION, Skill.PERCEPTION, Skill.SURVIVAL),

				EquipmentType.LIGHT_ARMOR, EquipmentType.SIMPLE_WEAPON,
				EquipmentType.HAND_CROSSBOW, EquipmentType.LONGSWORD,
				EquipmentType.RAPIER, EquipmentType.SHORTSWORD,
				EquipmentType.THIEVES_TOOLS,
				//TODO: Change into fighter equipment

				Ability.STRENGTH, Ability.CONSTITUTION));

			addAttribute("hit-points", new HitPointFeature(10));

			addAttribute("starting-equipment",
				new StartingEquipmentFeature(
					DataUtil.list(
						new EquipmentOption("a chain mail")
						),
					DataUtil.list(
						new EquipmentOption("a lether armor"),
						new EquipmentOption("a longbow"),
						new EquipmentOption("20 arrows", new PHBArrow(20))
						),
					DataUtil.list(
						new EquipmentOption("a martial weapon"),
						new EquipmentOption("a shield")
						),
					DataUtil.list(
						new EquipmentOption("two martial weapons")
						),
					DataUtil.list(
						new EquipmentOption("a light crossbow"),
						new EquipmentOption("20 bolts")
						),
					DataUtil.list(
						new EquipmentOption("two handaxes")
						),
					DataUtil.list(
						new EquipmentOption("a dungeoneer's pack")
						),
					DataUtil.list(
						new EquipmentOption("an explorer's pack")
						),
					DataUtil.list(
						new EquipmentOption("Leather armor, two daggers, and thieves' tools",
							new PHBLeatherArmor(), new PHBDagger(2), new PHBThievesTools())
						)
					)
				);
		}
	}

}
