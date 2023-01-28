package com.atelier.discord.embeds.dnd;

import com.atelier.discord.AtelierEmbedBuilder;
import com.atelier.dnd.character.AtelierCharacter;

public class AttributeListEmbedBuilder extends AtelierEmbedBuilder {
	public AttributeListEmbedBuilder(AtelierCharacter character) {
		StringBuilder content = new StringBuilder();
		setTitle(getMessage("title").addCharacter(character).get());
	
		character.getCharacterClass().getFeatures().values().forEach(a -> {
			content.append(getMessage("attribute")
				.add("attributeName", a.getName())
				.get());
			content.append("\n");
		});
		content.setLength(content.length() - 1);
		addField(character.getCharacterClass().toString(), content.toString(), true);

		content.setLength(0);
		character.getCharacterRace().getTraits().values().forEach(a -> {
			content.append(getMessage("attribute")
				.add("attributeName", a.getName())
				.get());
			content.append("\n");
		});
		content.setLength(content.length() - 1);
		addField(character.getCharacterRace().toString(), content.toString(), true);
	}
}
