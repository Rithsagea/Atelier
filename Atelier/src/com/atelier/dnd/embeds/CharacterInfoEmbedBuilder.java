package com.atelier.dnd.embeds;

import com.atelier.dnd.Ability;
import com.atelier.dnd.AbilitySpread;
import com.atelier.dnd.AtelierCharacter;

public class CharacterInfoEmbedBuilder extends AtelierEmbedBuilder {
	public CharacterInfoEmbedBuilder(AtelierCharacter character) {
		StringBuilder content = new StringBuilder();
		String prefix ;
		setTitle(getMessage("title").addCharacter(character).get());
		
		prefix = "";
		AbilitySpread spread = character.getAbilitySpread();
		for(Ability ability : Ability.values()) {
			content.append(prefix);
			content.append(getMessage("ability")
					.addAbility(ability)
					.add("baseScore", spread.getScore(ability))
					.get());
			prefix = "\n";
		}
		addField(getMessage("ability.title").get(), content.toString(), false);
	}
}
