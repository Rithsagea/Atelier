package com.tempera.atelier.discord.commands;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.data.DataObject;

public interface SlashAbstractCommand {
	public String getName();
	public String getDescription();
	public DataObject getData();
	
	public void execute(User user, SlashCommandInteractionEvent event);
	public void complete(CommandAutoCompleteInteractionEvent event);
}
