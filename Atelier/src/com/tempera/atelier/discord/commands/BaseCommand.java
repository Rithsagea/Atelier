package com.tempera.atelier.discord.commands;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class BaseCommand implements AbstractCommand {

	private String name;
	private String description;
	
	public BaseCommand(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public CommandData getData() {
		SlashCommandData data = Commands.slash(getName(), getDescription());
		addOptions(data);
		return data;
	}
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}
	public void addOptions(SlashCommandData data) {}
}
