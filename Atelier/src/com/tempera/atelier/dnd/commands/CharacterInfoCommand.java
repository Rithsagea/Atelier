package com.tempera.atelier.dnd.commands;

import java.util.List;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterInfoCommand extends CharacterSubCommand {

	public CharacterInfoCommand(AtelierDB db) {
		super(db);
	}

	@Override
	public String getLabel() {
		return "info";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(Sheet sheet, User user, List<String> args,
		MessageReceivedEvent event) {
		event.getChannel()
			.sendMessage(new CharacterInfoMessageBuilder(sheet).build())
			.queue();
	}

}
