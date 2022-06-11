package com.tempera.atelier.dnd.acommands.character;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.acommands.BaseCommand;
import com.tempera.atelier.discord.acommands.PermissionLevel;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class CharacterBaseCommand extends BaseCommand {
	
	public CharacterBaseCommand(AtelierBot bot, String label, List<String> aliases, PermissionLevel level) {
		super(label, aliases, level);
	}
		
	@Override
	public void execute(User user, List<String> args,
		MessageReceivedEvent event) {
		execute(user.getSheet(), user, args, event);
	}

	public abstract void execute(Sheet sheet, User user, List<String> args, MessageReceivedEvent event);

}
