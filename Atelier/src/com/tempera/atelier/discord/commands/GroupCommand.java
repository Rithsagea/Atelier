package com.tempera.atelier.discord.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class GroupCommand extends BaseCommand {

	private Map<String, AbstractSubcommand> subcommands = new HashMap<>();
	private Map<String, AbstractSubcommandGroup> groups = new HashMap<>();
	
	public GroupCommand(String name, String description) {
		super(name, description);
	}

	protected void registerSubcommand(AbstractSubcommand cmd) {
		subcommands.put(cmd.getName(), cmd);
	}
	
	protected void registerSubcommandGroup(AbstractSubcommandGroup group) {
		groups.put(group.getName(), group);
	}
	
	@Override
	public final void execute(User user, SlashCommandInteractionEvent event) {
		String group = event.getSubcommandGroup();
		String name = event.getSubcommandName();
		
		if(group == null) {
			AbstractSubcommand cmd = subcommands.get(name);
			if(cmd != null) cmd.execute(user, event);
		} else {
			AbstractSubcommandGroup cmd = groups.get(group);
			if(cmd != null) cmd.execute(user, event);
		}
	}
	
	@Override
	public final void complete(User user, CommandAutoCompleteInteractionEvent event) {
		String group = event.getSubcommandGroup();
		String name = event.getSubcommandName();
		
		if(group == null) {
			AbstractSubcommand cmd = subcommands.get(name);
			if(cmd != null) cmd.complete(user, event);
		} else {
			AbstractSubcommandGroup cmd = groups.get(group);
			if(cmd != null) cmd.complete(user, event);
		}
	}
	
	@Override
	public CommandData getData() {
		SlashCommandData data = (SlashCommandData) super.getData();
		
		data.addSubcommands(subcommands.values().stream().map(cmd -> cmd.getData()).collect(Collectors.toList()));
		data.addSubcommandGroups(groups.values().stream().map(cmd -> cmd.getData()).collect(Collectors.toList()));
		
		return data;
	}

	
}
