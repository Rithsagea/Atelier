package com.atelier.discord.commands.dnd.embeds;

import com.atelier.dnd.Ability;
import com.atelier.dnd.character.AtelierCharacter;
import com.atelier.util.WordUtil;

public class CharacterInfoEmbedBuilder extends AtelierEmbedBuilder {
	public CharacterInfoEmbedBuilder(AtelierCharacter character) {
		StringBuilder content = new StringBuilder();
		String prefix;
		
		setTitle(getMessage("title").addCharacter(character).get());
		
		prefix = "";
		for(Ability ability : Ability.values()) {
			content.append(prefix);
			content.append(getMessage("ability")
					.addAbility(ability)
					.add("baseScore", character.getBaseScore(ability))
					.add("abilityScore", character.getAbilityScore(ability))
					.add("abilityModifier", WordUtil.formatModifier(character.getAbilityModifier(ability)))
					.get());
			prefix = "\n";
		}
		addField(getMessage("ability.title").get(), content.toString(), false);
	}
}
