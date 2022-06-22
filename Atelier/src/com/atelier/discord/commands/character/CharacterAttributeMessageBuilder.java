package com.atelier.discord.commands.character;

import com.atelier.dnd.types.Sheet;
import com.atelier.dnd.types.character.AbstractClass;
import com.atelier.dnd.types.character.CharacterAttribute;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

public class CharacterAttributeMessageBuilder extends MessageBuilder {
	public CharacterAttributeMessageBuilder(Sheet sheet) {
		EmbedBuilder eb = new EmbedBuilder();
		StringBuilder content = new StringBuilder();
		
		eb.setTitle("Attributes");
		for(AbstractClass c : sheet.getClasses()) {
			for(CharacterAttribute a : c.getFeatures().values()) {
				content.append(a.getName() + "\n");
			}
			eb.addField(c.toString(), content.toString(), true);
			content.setLength(0);
		}
		
		setEmbeds(eb.build());
	}
}
