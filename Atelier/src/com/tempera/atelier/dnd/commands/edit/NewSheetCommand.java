package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.commands.AtelierCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NewSheetCommand implements AtelierCommand {

	private AtelierDB db;
	
	public NewSheetCommand(AtelierBot bot) {
		this.db = bot.getDatabase();
	}
	
	@Override
	public String getLabel() {
		return "new";
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
		Sheet sheet = new Sheet();
		db.addSheet(sheet);
		
		event.getChannel().sendMessageFormat("Created new sheet %s", sheet.getId()).queue();
	}
	
}
