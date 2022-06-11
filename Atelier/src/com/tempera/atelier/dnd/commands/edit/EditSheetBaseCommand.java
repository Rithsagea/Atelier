package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.acommands.BaseCommand;
import com.tempera.atelier.discord.acommands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class EditSheetBaseCommand extends BaseCommand {
	
	private AtelierDB db;
	
	public EditSheetBaseCommand(AtelierBot bot, String label, List<String> aliases, PermissionLevel level) {
		super(label, aliases, level);
		db = bot.getDatabase();
	}

	@Override
	public final void execute(User user, List<String> args, MessageReceivedEvent event) {
		Sheet sheet = db.getSheet(user.getSelectedSheetId());
		if(sheet != null) execute(user, sheet, args, event);
		else event.getChannel().sendMessage("Please select a sheet to edit").queue();
	}
	
	public abstract void execute(User user, Sheet sheet, List<String> args, MessageReceivedEvent event);
}
