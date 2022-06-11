package com.tempera.atelier.dnd.commands.edit;

import java.util.List;
import java.util.UUID;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.lcommands.BaseCommand;
import com.tempera.atelier.discord.lcommands.CommandRegistry;
import com.tempera.atelier.discord.lcommands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Campaign;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditCampaignCommand extends EditCampaignGroupCommand {

	private class ListCampaign extends BaseCommand {

		private AtelierDB db;

		public ListCampaign(AtelierBot bot) {
			super("list", DataUtil.list("l"), PermissionLevel.ADMINISTRATOR);
			db = bot.getDatabase();
		}

		@Override
		public void execute(User user, List<String> args, MessageReceivedEvent event) {
			MessageBuilder b = new MessageBuilder();
			for (Campaign campaign : db.listCampaigns()) {
				b.append(campaign);
				b.append("\n");
			}

			event.getChannel().sendMessage(b.build()).queue();
		}
	}

	private class NewCampaign extends BaseCommand {
		private AtelierDB db;

		public NewCampaign(AtelierBot bot) {
			super("new", null, PermissionLevel.ADMINISTRATOR);

			db = bot.getDatabase();
		}

		@Override
		public void execute(User user, List<String> args, MessageReceivedEvent event) {
			Campaign campaign = new Campaign();
			db.addCampaign(campaign);

			event.getChannel().sendMessageFormat("Created new campaign %s", campaign.getId()).queue();
		}
	}

	private class SelectCampaign extends BaseCommand {

		private AtelierDB db;
		
		public SelectCampaign(AtelierBot bot) {
			super("select", null, PermissionLevel.ADMINISTRATOR);
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
				user.setEditingCampaign(campaign);
				event.getChannel().sendMessage("Selected campaign: " + campaign).queue();
				return;
			}
		}
		
	}
	
	private class NameCampaign extends EditCampaignBaseCommand {
		public NameCampaign(AtelierBot bot) {
			super(bot, "name", null, PermissionLevel.ADMINISTRATOR);
		}

		@Override
		public void execute(User user, Campaign campaign, List<String> args, MessageReceivedEvent event) {
			if (args.size() >= 2) {
				campaign.setName(args.get(1));
				event.getChannel().sendMessage("Set campaign's name to " + args.get(1)).queue();
				return;
			}

			event.getChannel().sendMessage("Campaign name: " + campaign.getName()).queue();
		}
	}

	
	public EditCampaignCommand(AtelierBot bot) {
		super(bot, "campaign", DataUtil.list("c"), PermissionLevel.ADMINISTRATOR);

		CommandRegistry reg = getCommandRegistry();
		reg.registerCommand(new ListCampaign(bot));
		reg.registerCommand(new NewCampaign(bot));
		reg.registerCommand(new SelectCampaign(bot));
		
		reg.registerCommand(new NameCampaign(bot));
	}

	@Override
	public void executeDefault(User user, Campaign campaign, List<String> args, MessageReceivedEvent event) {
		event.getChannel().sendMessage("Currently editing: " + campaign).queue();
	}

}
