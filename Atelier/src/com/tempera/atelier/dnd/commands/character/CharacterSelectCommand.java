package com.tempera.atelier.dnd.commands.character;

import java.util.List;
import java.util.UUID;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.lcommands.BaseCommand;
import com.tempera.atelier.discord.lcommands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterSelectCommand extends BaseCommand {

	private AtelierDB db;
	
	public CharacterSelectCommand(AtelierBot bot) {
		super("select", null, PermissionLevel.USER);
		db = bot.getDatabase();
	}

	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		if (args.size() < 2) {
			event.getChannel().sendMessage("Specify a valid sheet id").queue();
			return;
		}

		UUID id;

		try {
			id = UUID.fromString(args.get(1));
		} catch (IllegalArgumentException e) {
			event.getChannel().sendMessage("Invalid id format").queue();
			return;
		}

		Sheet sheet = db.getSheet(id);
		if (sheet != null) {
			user.setSheet(sheet);
			event.getChannel().sendMessage("Selected sheet: " + sheet).queue();
			return;
		}
	}

}
