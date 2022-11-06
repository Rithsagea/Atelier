package com.atelier.dnd.embeds;

import com.atelier.dnd.character.AtelierCharacter;

public class CharacterAttributesEmbedBuilder extends AtelierEmbedBuilder {
	public CharacterAttributesEmbedBuilder(AtelierCharacter character) {
		StringBuilder content = new StringBuilder();
		setTitle(getMessage("title").addCharacter(character).get());
	
		character.getAttributes().forEach(a -> {
			content.append(getMessage("attribute")
				.add("attributeName", a.getName())
				.get());
			content.append("\n");
		});
		content.setLength(content.length() - 1);
		setDescription(content.toString());
	}
}
