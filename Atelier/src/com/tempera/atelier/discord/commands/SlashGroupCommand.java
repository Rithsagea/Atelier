package com.tempera.atelier.discord.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class SlashGroupCommand extends SlashBaseCommand {

	private Map<String, SlashAbstractSubcommand> subcommands = new HashMap<>();
	
	public SlashGroupCommand(String name, String description) {
		super(name, description);
	}

	protected void registerSubcommand(SlashAbstractSubcommand cmd) {
		subcommands.put(cmd.getName(), cmd);
	}
	
	@Override
	public final void execute(User user, SlashCommandInteractionEvent event) {
		String group = event.getSubcommandGroup();
		String name = event.getSubcommandName();
		
		if(group == null) {
			SlashAbstractSubcommand cmd = subcommands.get(name);
			if(cmd != null) cmd.execute(user, event);
		} else {
			//TODO: group commands
		}
	}
	
	@Override
	public final void complete(User user, CommandAutoCompleteInteractionEvent event) {
		String group = event.getSubcommandGroup();
		String name = event.getSubcommandName();
		
		if(group == null) {
			SlashAbstractSubcommand cmd = subcommands.get(name);
			if(cmd != null) cmd.complete(user, event);
		} else {
			//TODO: group commands
		}
	}
	
	@Override
	public CommandData getData() {
		SlashCommandData data = (SlashCommandData) super.getData();
		
		data.addSubcommands(subcommands.values().stream().map(cmd -> cmd.getData()).collect(Collectors.toList()));
		
		return data;
	}

	
}
