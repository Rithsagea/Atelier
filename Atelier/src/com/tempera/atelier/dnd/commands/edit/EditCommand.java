package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.CommandRegistry;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditCommand extends GroupCommand {

	public EditCommand(AtelierBot bot) {
		super("edit", DataUtil.list("e"), PermissionLevel.ADMINISTRATOR);
		
		CommandRegistry reg = getCommandRegistry();
		reg.registerCommand(new EditSheetCommand(bot));
		reg.registerCommand(new EditCampaignCommand(bot));
		reg.registerCommand(new EditUserCommand(bot));
	}
	
	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) { }

}
