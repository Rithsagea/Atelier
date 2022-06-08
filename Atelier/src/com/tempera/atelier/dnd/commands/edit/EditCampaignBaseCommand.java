package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.BaseCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Campaign;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class EditCampaignBaseCommand extends BaseCommand {
	
	private AtelierDB db;
	
	public EditCampaignBaseCommand(AtelierBot bot, String label, List<String> aliases, PermissionLevel level) {
		super(label, aliases, level);
		
		db = bot.getDatabase();
	}

	@Override
	public final void execute(User user, List<String> args, MessageReceivedEvent event) {
		Campaign campaign = db.getCampaign(user.getSelectedCampaignId());
		execute(user, campaign, args, event);
	}

	public abstract void execute(User user, Campaign campaign, List<String> args, MessageReceivedEvent event);
}