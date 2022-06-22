package com.atelier.discord.commands;

import com.atelier.AtelierBot;
import com.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class StopCommand extends BaseCommand {

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
