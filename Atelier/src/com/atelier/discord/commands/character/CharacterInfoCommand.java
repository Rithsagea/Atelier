package com.atelier.discord.commands.character;

import com.atelier.discord.User;
import com.atelier.discord.commands.BaseSubcommand;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CharacterInfoCommand extends BaseSubcommand {

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
