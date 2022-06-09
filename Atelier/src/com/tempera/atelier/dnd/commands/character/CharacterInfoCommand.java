package com.tempera.atelier.dnd.commands.character;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterInfoCommand extends CharacterBaseCommand {

	public CharacterInfoCommand(AtelierBot bot) {
		super(bot, "info", null, PermissionLevel.USER);
	}

	@Override
	public void execute(Sheet sheet, User user, List<String> args,
		MessageReceivedEvent event) {
		event.getChannel()
			.sendMessage(new CharacterInfoMessageBuilder(sheet).build())
			.queue();
	}

}
