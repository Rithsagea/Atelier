package com.atelier.dnd.commands.campaign;

import com.atelier.discord.User;
import com.atelier.discord.commands.BaseInteraction.BaseSubcommand;
import com.atelier.dnd.types.Campaign;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CampaignInfoCommand extends BaseSubcommand {
	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		Campaign campaign = user.getCampaign();
		event.getChannel().sendMessage("Campaign: " + campaign).queue();
	}
}
