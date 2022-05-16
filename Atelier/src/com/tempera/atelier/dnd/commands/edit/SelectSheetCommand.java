package com.tempera.atelier.dnd.commands.edit;

import java.util.List;
import java.util.UUID;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.commands.AtelierCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SelectSheetCommand implements AtelierCommand {

	private AtelierDB db;
	
	public SelectSheetCommand(AtelierBot bot) {
		this.db = bot.getDatabase();
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
		return PermissionLevel.USER;
	}

	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		if(args.size() < 2) {
			event.getChannel().sendMessage("Specify a valid sheet id").queue();
			return;
		}
		
		UUID id;
		
		try {
			id = UUID.fromString(args.get(1));
		} catch(IllegalArgumentException e) {
			event.getChannel().sendMessage("Invalid id format").queue();
			return;
		}
		
		Sheet sheet = db.getSheet(id);
		if(sheet != null) {
			user.setSelectedSheet(sheet);
			event.getChannel().sendMessage("Selected sheet: " + sheet).queue();
			return;
		}
	}

}
