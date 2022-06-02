package com.tempera.atelier.dnd.types.character.classes;

import com.rithsagea.util.choice.Choice;
import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.character.CharacterClass;
import com.tempera.atelier.dnd.types.character.features.HitPointFeature;
import com.tempera.atelier.dnd.types.character.features.ProficiencyFeature;
import com.tempera.atelier.dnd.types.character.features.StartingEquipmentFeature;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.enums.Skill;

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

			addAttribute("starting-equipment", new StartingEquipmentFeature());
		}
	}
}
