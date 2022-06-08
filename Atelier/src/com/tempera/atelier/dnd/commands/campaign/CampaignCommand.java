package com.tempera.atelier.dnd.commands.campaign;

import java.util.List;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CampaignCommand extends GroupCommand {

	public CampaignCommand() {
		super("campaign", DataUtil.list("c"), PermissionLevel.ADMINISTRATOR);
	}
	
	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		event.getChannel().sendMessage("I am alive").queue();
	}
	
}
