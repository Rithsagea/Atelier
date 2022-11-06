package com.atelier.dnd.embeds;

import com.atelier.discord.AtelierUser;
import com.atelier.dnd.character.AtelierCharacter;

public class CharacterListEmbedBuilder extends AtelierEmbedBuilder {
	public CharacterListEmbedBuilder(AtelierUser user) {
		setTitle(getMessage("title").addUser(user).get());
		for(AtelierCharacter character : user.getCharacters())
			appendDescription(character + "\n");
	}
}
