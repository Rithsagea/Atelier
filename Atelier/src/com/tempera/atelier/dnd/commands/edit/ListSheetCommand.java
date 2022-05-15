package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.commands.AtelierCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ListSheetCommand implements AtelierCommand {

	private AtelierDB db;
	
	public ListSheetCommand(AtelierBot bot) {
		db = bot.getDatabase();
	}
	
	@Override
	public String getLabel() {
		return "list";
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
		MessageBuilder b = new MessageBuilder();
		for(Sheet sheet : db.listSheets()) {
			b.appendFormat("%s [%s]\n", sheet.getId(), sheet.getName());
		}
		
		event.getChannel().sendMessage(b.build()).queue();
	}
	
}
