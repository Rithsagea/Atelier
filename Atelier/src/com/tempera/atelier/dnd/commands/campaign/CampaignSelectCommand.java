package com.tempera.atelier.dnd.commands.campaign;

import java.util.UUID;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.BaseSubcommand;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Campaign;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CampaignSelectCommand extends BaseSubcommand {
	
	private AtelierDB db = AtelierDB.getInstance();
	
	public CampaignSelectCommand() {
		super("select", "selects a campaign to join");
	}

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
