package com.tempera.atelier.discord.commands.character;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.SlashBaseSubcommand;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SlashCharacterListCommand extends SlashBaseSubcommand {

	public SlashCharacterListCommand() {
		super("list", "Lists the user's available character sheets");
	}

	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		event.reply(new CharacterListMessageBuilder(user).build()).setEphemeral(true).queue();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) { }

	@Override
	public void addOptions(SubcommandData data) { }

}
