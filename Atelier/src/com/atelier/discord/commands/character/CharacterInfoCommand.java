package com.atelier.discord.commands.character;

import com.atelier.discord.User;
import com.atelier.discord.commands.BaseInteraction.BaseSubcommand;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CharacterInfoCommand extends BaseSubcommand {

	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		event.reply(new CharacterInfoMessageBuilder(user.getSheet()).build()).setEphemeral(true).queue();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) { }

}
