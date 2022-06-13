package com.tempera.atelier.discord.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public abstract class SlashBaseSubcommandGroup implements SlashAbstractSubcommandGroup {
	private String name;
	private String description;
	
	private Map<String, SlashAbstractSubcommand> subcommands = new HashMap<>();
	
	public SlashBaseSubcommandGroup(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	protected void registerSubcommand(SlashAbstractSubcommand cmd) {
		subcommands.put(cmd.getName(), cmd);
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
	public SubcommandGroupData getData() {
		SubcommandGroupData data = new SubcommandGroupData(getName(), getDescription());
		
		data.addSubcommands(subcommands.values().stream().map(cmd -> cmd.getData()).collect(Collectors.toList()));
		
		return data;
	}
	
	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		subcommands.get(event.getSubcommandName()).execute(user, event);
	}
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {
		subcommands.get(event.getSubcommandName()).complete(user, event);
	}
	
	public final void addOptions(SubcommandData data) {}
}
