package com.atelier.dnd.embeds;

import com.atelier.dnd.AtelierCharacter;

public class CharacterInfoEmbedBuilder extends AtelierEmbed {
	public CharacterInfoEmbedBuilder(AtelierCharacter character) {
		setTitle(getMessage("title").addCharacter(character).get());
	}
}
