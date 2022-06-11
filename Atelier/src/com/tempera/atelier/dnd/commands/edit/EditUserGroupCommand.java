package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.acommands.GroupCommand;
import com.tempera.atelier.discord.acommands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class EditUserGroupCommand extends GroupCommand {

	private AtelierDB db;
	
	public EditUserGroupCommand(AtelierBot bot, String label, List<String> aliases, PermissionLevel level) {
		super(label, aliases, level);
		db = bot.getDatabase();
	}

	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		if(args.size() > 1) {
			long id = -1;
			
			try {
				id = Long.parseLong(args.get(1));
			} catch (NumberFormatException e) { }
			try {
				id = Long.parseLong(args.get(1).substring(2, 20));
			} catch (NumberFormatException e) { }
			
			User target = db.getUser(id);
			if(target != null) {
				executeDefault(user, target, args, event);
				return;
			}
		}
		event.getChannel().sendMessage("Please input a valid user or user id").queue();
	}
	
	public abstract void executeDefault(User user, User target, List<String> args, MessageReceivedEvent event);
	
}
