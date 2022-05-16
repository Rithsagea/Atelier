package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.discord.commands.BaseCommand;
import com.tempera.atelier.discord.commands.CommandRegistry;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.types.spread.AbilitySpread;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditStatCommand extends GroupCommand {

	private class Type extends BaseCommand {

		public Type() {
			super("type", null, PermissionLevel.ADMINISTRATOR);
		}

		@Override
		public void execute(User user, List<String> args, MessageReceivedEvent event) {
			
		}
		
	}
	
	public EditStatCommand() {
		CommandRegistry reg = getCommandRegistry();
		reg.registerCommand(new Type());
	}
	
	@Override
	public String getLabel() {
		return "stat";
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
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		Sheet sheet = user.getSelectedSheet();
		
		AbilitySpread spread = sheet.getBaseScores();
		if(spread == null) {
			event.getChannel().sendMessage("This sheet does not have an ability spread").queue();
		}
	}

}
