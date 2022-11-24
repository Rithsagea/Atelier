package com.atelier.discord.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.atelier.discord.AtelierUser;
import com.atelier.util.AtelierLanguageManager;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public abstract class BaseInteraction implements AbstractInteraction {
	
	private String name;
	private String description;
	
	public BaseInteraction() {
		name = AtelierLanguageManager.getInstance().get(this, "name");
		description = AtelierLanguageManager.getInstance().get(this, "description");
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
	public void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event) {}
	
	public static abstract class BaseCommand extends BaseInteraction implements AbstractCommand {	
		public void addOptions(SlashCommandData data) {}
		
		@Override
		public CommandData getData() {
			SlashCommandData data = Commands.slash(getName(), getDescription());
			addOptions(data);
			return data;
		}
	}
	
	public static abstract class BaseSubcommand extends BaseInteraction implements AbstractSubcommand {
		public void addOptions(SubcommandData data) {}
		
		@Override
		public SubcommandData getData() {
			SubcommandData data = new SubcommandData(getName(), getDescription());
			addOptions(data);
			return data;
		}
	}

	public static abstract class BaseSubcommandGroup extends BaseInteraction implements AbstractSubcommandGroup {
		private Map<String, AbstractSubcommand> subcommands = new HashMap<>();
		
		protected void registerSubcommand(AbstractSubcommand cmd) {
			subcommands.put(cmd.getName(), cmd);
		}
		
		@Override
		public SubcommandGroupData getData() {
			SubcommandGroupData data = new SubcommandGroupData(getName(), getDescription());
			data.addSubcommands(subcommands.values().stream().map(cmd -> cmd.getData()).collect(Collectors.toList()));
			return data;
		}
		
		@Override
		public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
			subcommands.get(event.getSubcommandName()).execute(user, event);
		}
		
		@Override
		public void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event) {
			subcommands.get(event.getSubcommandName()).complete(user, event);
		}
		
		public final void addOptions(SubcommandData data) {}
	}

	public static abstract class GroupCommand extends BaseCommand {
		private Map<String, AbstractSubcommand> subcommands = new HashMap<>();
		private Map<String, AbstractSubcommandGroup> groups = new HashMap<>();

		protected void registerSubcommand(AbstractSubcommand cmd) {
			subcommands.put(cmd.getName(), cmd);
		}
		
		protected void registerSubcommandGroup(AbstractSubcommandGroup group) {
			groups.put(group.getName(), group);
		}
		
		@Override
		public final void execute(AtelierUser user, SlashCommandInteractionEvent event) {
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
		public final void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event) {
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
}
