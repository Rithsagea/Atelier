package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.commands.CommandRegistry;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditCommand extends GroupCommand {
	
	public EditCommand(AtelierBot bot) {
		CommandRegistry reg = getCommandRegistry();
		reg.registerCommand(new ListSheetCommand(bot));
		reg.registerCommand(new NewSheetCommand(bot));
		reg.registerCommand(new SelectSheetCommand(bot));
		
		reg.registerCommand(new EditNameCommand());
		reg.registerCommand(new EditStatCommand(bot));
	}
	
	@Override
	public String getLabel() {
		return "edit";
	}

	@Override
	public List<String> getAliases() {
		return DataUtil.list("e");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.ADMINISTRATOR;
	}

	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		event.getChannel().sendMessage("Currently editing: " + user.getSelectedSheet()).queue();
	}

}
