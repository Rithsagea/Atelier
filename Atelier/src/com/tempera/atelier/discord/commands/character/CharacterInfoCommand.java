package com.tempera.atelier.discord.commands.character;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.SlashBaseSubcommand;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CharacterInfoCommand extends SlashBaseSubcommand {

	public CharacterInfoCommand() {
		super("info", "Gets an overview of the selected character sheet");
	}

	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		event.reply(new CharacterInfoMessageBuilder(user.getSheet()).build()).setEphemeral(true).queue();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) { }

}
