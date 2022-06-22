package com.atelier.discord.commands;

import com.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public interface AbstractSubcommand {
	public String getName();
	public String getDescription();
	public SubcommandData getData();
	
	public void execute(User user, SlashCommandInteractionEvent event);
	public void complete(User user, CommandAutoCompleteInteractionEvent event);
}
