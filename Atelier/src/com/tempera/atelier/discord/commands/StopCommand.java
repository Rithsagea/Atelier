package com.tempera.atelier.discord.commands;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class StopCommand extends SlashBaseCommand {

	private AtelierBot bot;

	public StopCommand(AtelierBot bot) {
		super("stop", "Stops the bot");
		
		this.bot = bot;
	}

	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		event.reply("Stopping AtelierBot!").setEphemeral(true).queue(m -> bot.stop());
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {
		
	}
}
