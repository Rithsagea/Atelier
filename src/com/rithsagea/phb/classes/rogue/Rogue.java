package com.rithsagea.phb.classes.rogue;

import java.util.LinkedHashMap;
import java.util.Map;

import com.atelier.database.AtelierType;
import com.atelier.dnd.Ability;
import com.atelier.dnd.Skill;
import com.atelier.dnd.character.attributes.CharacterClass;
import com.atelier.dnd.character.attributes.ClassFeature;
import com.atelier.dnd.character.attributes.ProficiencyFeature;

@AtelierType("rogue")
public class Rogue extends CharacterClass {

	@Override
	protected Map<String, ClassFeature> getFeatures(int level) {
		Map<String, ClassFeature> map = new LinkedHashMap<>();

		switch(level) {
			case 0:
				map.put("0-proficiency", new ProficiencyFeature(4,
					Ability.DEXTERITY, Ability.INTELLIGENCE,
					Skill.ACROBATICS, Skill.ATHLETICS, Skill.DECEPTION, Skill.INSIGHT,
					Skill.INTIMIDATION, Skill.INVESTIGATION, Skill.PERCEPTION, Skill.PERFORMANCE,
					Skill.PERSUASION, Skill.SLEIGHT_OF_HAND, Skill.STEALTH));
			case 1:
				map.put("1-expertise", new RogueExpertiseFeature());
				map.put("1-sneak-attack", new SneakAttackFeature());
				map.put("1-thieves-cant", new ThievesCantFeature());
				break;
		}

		return map;
	}
}
