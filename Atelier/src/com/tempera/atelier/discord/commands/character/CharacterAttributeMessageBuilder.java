package com.tempera.atelier.discord.commands.character;

import com.tempera.atelier.dnd.types.Sheet;
import com.tempera.atelier.dnd.types.character.AbstractClass;
import com.tempera.atelier.dnd.types.character.CharacterAttribute;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

public class CharacterAttributeMessageBuilder extends MessageBuilder {
	public CharacterAttributeMessageBuilder(Sheet sheet) {
		EmbedBuilder eb = new EmbedBuilder();
		StringBuilder content = new StringBuilder();
		
		eb.setTitle("Attributes");
		for(AbstractClass c : sheet.getClasses()) {
			for(CharacterAttribute a : c.getAttributeMap().values()) {
				content.append(a.getName() + "\n");
			}
			eb.addField(c.toString(), content.toString(), true);
			content.setLength(0);
		}
		
		setEmbeds(eb.build());
	}
}
