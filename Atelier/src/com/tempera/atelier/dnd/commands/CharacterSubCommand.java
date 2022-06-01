package com.tempera.atelier.dnd.commands;

import java.util.List;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.AtelierCommand;
import com.tempera.atelier.dnd.database.AtelierDB;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class CharacterSubCommand implements AtelierCommand {

	private AtelierDB db;
	
	public CharacterSubCommand(AtelierDB db) {
		this.db = db;
	}
	
	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		execute(db.getSheet(user.getSheetId()), user, args, event);
	}
	
	public abstract void execute(Sheet sheet, User user, List<String> args, MessageReceivedEvent event);

}
