package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditCampaignCommand extends GroupCommand {

	public EditCampaignCommand(AtelierBot bot) {
		
	}
	
	@Override
	public String getLabel() {
		return "campaign";
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
		
	}

}
