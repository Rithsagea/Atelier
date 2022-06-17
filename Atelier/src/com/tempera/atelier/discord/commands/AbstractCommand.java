package com.tempera.atelier.discord.commands;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface AbstractCommand {
	public String getName();
	public String getDescription();
	public CommandData getData();
	
	public void execute(User user, SlashCommandInteractionEvent event);
	public void complete(User user, CommandAutoCompleteInteractionEvent event);
}
