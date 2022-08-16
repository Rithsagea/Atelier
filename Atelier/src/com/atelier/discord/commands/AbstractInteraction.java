package com.atelier.discord.commands;

import com.atelier.discord.AtelierUser;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public interface AbstractInteraction {
	public String getName();
	public String getDescription();
	
	public void execute(AtelierUser user, SlashCommandInteractionEvent event);
	public void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event);
	
	public static interface AbstractCommand extends AbstractInteraction {
		public CommandData getData();
	}
	
	public static interface AbstractSubcommand extends AbstractInteraction {
		public SubcommandData getData();
	}
	
	public static interface AbstractSubcommandGroup extends AbstractInteraction {
		public SubcommandGroupData getData();
	}

}
