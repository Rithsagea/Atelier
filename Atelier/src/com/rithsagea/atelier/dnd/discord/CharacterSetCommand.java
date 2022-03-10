package com.rithsagea.atelier.dnd.discord;

import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.AtelierCommand;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterSetCommand implements AtelierCommand {

	private AtelierDB db;
	
	public CharacterSetCommand(AtelierBot bot) {
		db = bot.getDatabase();
	}
	
	@Override
	public String getLabel() {
		return "set";
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
		Sheet sheet = db.getSheet(user.getSheetId());
		
		switch(args.get(1)) {
		
		case "name":
			sheet.setName(args.get(2));
			event.getChannel().sendMessage("Changed name to: " + args.get(2)).queue();
			break;
			
		}
	}

}
