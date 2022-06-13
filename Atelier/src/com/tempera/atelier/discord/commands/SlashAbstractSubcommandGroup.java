package com.tempera.atelier.discord.commands;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public interface SlashAbstractSubcommandGroup {
	public String getName();
	public String getDescription();
	public SubcommandGroupData getData();
	
	public void execute(User user, SlashCommandInteractionEvent event);
	public void complete(User user, CommandAutoCompleteInteractionEvent event);
}
