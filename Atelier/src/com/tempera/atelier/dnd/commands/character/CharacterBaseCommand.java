package com.tempera.atelier.dnd.commands.character;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.lcommands.BaseCommand;
import com.tempera.atelier.discord.lcommands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class CharacterBaseCommand extends BaseCommand {

	private AtelierDB db;

	public CharacterBaseCommand(AtelierBot bot, String label, List<String> aliases, PermissionLevel level) {
		super(label, aliases, level);
		this.db = bot.getDatabase();
	}
		
	@Override
	public void execute(User user, List<String> args,
		MessageReceivedEvent event) {
		execute(db.getSheet(user.getSheetId()), user, args, event);
	}

	public abstract void execute(Sheet sheet, User user, List<String> args, MessageReceivedEvent event);

}
