package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Campaign;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class EditCampaignGroupCommand extends GroupCommand {
	
	private AtelierDB db;
	
	public EditCampaignGroupCommand(AtelierBot bot, String label, List<String> aliases, PermissionLevel level) {
		super(label, aliases, level);
		
		db = bot.getDatabase();
	}

	@Override
	public final void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		Campaign campaign = db.getCampaign(user.getSelectedCampaignId());
		if(campaign != null) executeDefault(user, campaign, args, event);
		else event.getChannel().sendMessage("Please select a campaign to edit").queue();
	}

	public abstract void executeDefault(User user, Campaign campaign, List<String> args, MessageReceivedEvent event);
}