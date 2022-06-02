package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.AtelierCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditNameCommand implements AtelierCommand {

	@Override
	public String getLabel() {
		return "name";
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
	public void execute(User user, List<String> args,
		MessageReceivedEvent event) {
		if (args.size() >= 2) {
			user.getSelectedSheet()
				.setName(args.get(1));
			event.getChannel()
				.sendMessage("Set character's name to " + args.get(1))
				.queue();
			return;
		}

		event.getChannel()
			.sendMessage("Character name: " + user.getSelectedSheet()
				.getName())
			.queue();
	}

}
