package com.atelier.discord.commands;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public abstract class BaseSubcommand implements AbstractSubcommand {
	private String name;
	private String description;
	
	public BaseSubcommand(String name, String description) {
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
	public SubcommandData getData() {
		SubcommandData data = new SubcommandData(getName(), getDescription());
		addOptions(data);
		return data;
	}
	
	public void addOptions(SubcommandData data) {}
}
