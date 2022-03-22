package com.rithsagea.atelier.dnd.discord.character;

import java.util.List;
import java.util.UUID;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.AtelierCommand;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterSelectCommand implements AtelierCommand {

	private AtelierDB db;
	
	public CharacterSelectCommand(AtelierBot bot) {
		db = bot.getDatabase();
	}
	
	@Override
	public String getLabel() {
		return "select";
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
		UUID sheetId = UUID.fromString(args.get(1));
		Sheet sheet = db.getSheet(sheetId);
		
		if(sheet != null) {
			user.setSheetId(sheetId);
			event.getChannel().sendMessage("Sheet Changed to " + sheetId).queue();
		} else {
			event.getChannel().sendMessage("Sheet does not exist!").queue();
		}
		
		user.setName(event.getAuthor().getName());
	}

}
