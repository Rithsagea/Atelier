package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.commands.AtelierCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.types.character.Attribute;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditAttributeCommand implements AtelierCommand  {

	public EditAttributeCommand(AtelierBot bot) {
		
	}
	
	@Override
	public String getLabel() {
		return "attribute";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.ADMINISTRATOR;
	}

	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		
		MessageBuilder builder = new MessageBuilder();

		for(Attribute attribute : user.getSelectedSheet().getAttributes()) {
			builder.appendFormat("%s\n", attribute.getName());
		}
		
		event.getChannel().sendMessage(builder.build()).queue();
	}

}
