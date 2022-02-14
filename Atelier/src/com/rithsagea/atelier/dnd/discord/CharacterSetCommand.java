package com.rithsagea.atelier.dnd.discord;

import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.AtelierCommand;
import com.rithsagea.atelier.dnd.database.AtelierDB;
import com.rithsagea.atelier.dnd.database.Sheet;
import com.rithsagea.atelier.dnd.database.User;

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
	public void execute(List<String> args, MessageReceivedEvent event) {
		long id = event.getMember().getIdLong();
		User user = db.findUser(id);
		Sheet sheet = db.findSheet(user.getSheetId());
		
		switch(args.get(1)) {
		
		case "name":
			sheet.setName(args.get(2));
			event.getChannel().sendMessage("Changed name to: " + args.get(2)).queue();
			break;
			
		}
		
		db.updateSheet(sheet);
	}

}
