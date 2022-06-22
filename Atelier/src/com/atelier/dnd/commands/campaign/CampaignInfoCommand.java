package com.atelier.dnd.commands.campaign;

import com.atelier.discord.User;
import com.atelier.discord.commands.BaseSubcommand;
import com.atelier.dnd.types.Campaign;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CampaignInfoCommand extends BaseSubcommand {
	public CampaignInfoCommand() {
		super("info", "gets info about the selected campaign");
	}

	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		Campaign campaign = user.getCampaign();
		event.getChannel().sendMessage("Campaign: " + campaign).queue();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}
}
