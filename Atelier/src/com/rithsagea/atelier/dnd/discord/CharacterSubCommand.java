package com.rithsagea.atelier.dnd.discord;

import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.AtelierCommand;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class CharacterSubCommand implements AtelierCommand {
	
	private AtelierDB db;
	
	public CharacterSubCommand(AtelierBot bot) {
		db = bot.getDatabase();
	}
	
	@Override
	public final void execute(User user, List<String> args, MessageReceivedEvent event) {
		Sheet sheet = db.getSheet(user.getSheetId());
		
		execute(user, sheet, args, event);
	}
	
	public abstract void execute(User user, Sheet sheet, List<String> args, MessageReceivedEvent event);
}
