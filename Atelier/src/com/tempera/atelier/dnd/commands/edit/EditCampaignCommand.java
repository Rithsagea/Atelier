package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.BaseCommand;
import com.tempera.atelier.discord.commands.CommandRegistry;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Campaign;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditCampaignCommand extends GroupCommand {

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

	public EditCampaignCommand(AtelierBot bot) {
		super("campaign", DataUtil.list("c"), PermissionLevel.ADMINISTRATOR);

		CommandRegistry reg = getCommandRegistry();
		reg.registerCommand(new ListCampaign(bot));
		reg.registerCommand(new NewCampaign(bot));
	}

	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {

	}

}
