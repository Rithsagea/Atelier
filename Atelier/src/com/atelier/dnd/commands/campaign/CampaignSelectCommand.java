package com.atelier.dnd.commands.campaign;

import java.util.UUID;

import com.atelier.database.AtelierDB;
import com.atelier.discord.User;
import com.atelier.discord.commands.BaseInteraction.BaseSubcommand;
import com.atelier.dnd.types.Campaign;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CampaignSelectCommand extends BaseSubcommand {
	
	private AtelierDB db = AtelierDB.getInstance();

	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		UUID id;

		try {
			id = UUID.fromString(event.getOption("id").getAsString());
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

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}	
	
	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.STRING, "id", "the id of the campaign", true);
	}
}
