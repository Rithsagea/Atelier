package com.tempera.atelier.dnd.commands.campaign;

import java.util.List;
import java.util.UUID;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.BaseCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Campaign;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CampaignSelectCommand extends BaseCommand {
	
	private AtelierDB db;
	
	public CampaignSelectCommand(AtelierBot bot) {
		super("select", null, PermissionLevel.USER);
		db = bot.getDatabase();
	}

	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		if (args.size() < 2) {
			event.getChannel().sendMessage("Specify a valid campaign id").queue();
			return;
		}
		
		UUID id;

		try {
			id = UUID.fromString(args.get(1));
		} catch (IllegalArgumentException e) {
			event.getChannel().sendMessage("Invalid id format").queue();
			return;
		}

		Campaign campaign = db.getCampaign(id);
		if (campaign != null) {
			user.setCampaign(campaign);
			event.getChannel().sendMessage("Selected campaign: " + campaign).queue();
			return;
		}
	}
	
	
}
