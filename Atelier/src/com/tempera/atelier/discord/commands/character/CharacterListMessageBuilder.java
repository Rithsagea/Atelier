package com.tempera.atelier.discord.commands.character;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

public class CharacterListMessageBuilder extends MessageBuilder {
	public CharacterListMessageBuilder(User user) {
		EmbedBuilder eb = new EmbedBuilder();
		StringBuilder content = new StringBuilder();
		
		for(Sheet sheet : user.getSheets()) {
			content.append("Id: " + sheet.getId() + "\n");
			content.append("Classes: " + sheet.getClasses());
			eb.addField(sheet.getName(), content.toString(), false);
			content.setLength(0);
		}
		
		setEmbeds(eb.build());
	}
}
