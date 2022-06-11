package com.tempera.atelier.discord.commands;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class SlashBaseCommand implements SlashAbstractCommand {

	private String name;
	private String description;
	
	public SlashBaseCommand(String name, String description) {
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
	
	public void addOptions(SlashCommandData data) {}
}
