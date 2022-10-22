package com.atelier.discord.listeners;

import org.slf4j.Logger;

import com.atelier.AtelierBot;
import com.atelier.Commands;
import com.atelier.Config;
import com.atelier.database.AtelierDB;
import com.atelier.discord.AtelierUser;
import com.atelier.discord.Menu;
import com.atelier.discord.MenuManager;
import com.atelier.discord.commands.CommandRegistry;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	private CommandRegistry reg = new CommandRegistry();
	
	private AtelierDB db = AtelierDB.getInstance();
	private MenuManager menus = MenuManager.getInstance();
	
	private Logger log;
	
	public CommandListener() {
		this.log = AtelierBot.getInstance().getLogger();
		Commands.registerDiscordCommands(reg);
	}
	
	public CommandRegistry getRegistry() {
		return reg;
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		//testing commands
		Guild guild = event.getJDA().getGuildById(Config.getInstance().getTestingGuildId());
		
		//delete all commands
		// guild.retrieveCommands().queue(list -> list.forEach(cmd -> cmd.delete().queue()));
		
		reg.getCommands().stream()
			.map(cmd -> guild.upsertCommand(cmd.getData()))
			.forEach(cmd -> cmd.queue());
		
		//global commands
		// JDA jda = event.getJDA();
		
		//delete all commands
		// jda.retrieveCommands().queue(list -> list.forEach(cmd -> cmd.delete().queue()));

		// Stream.of(reg.getCommand("music"))
		// 	.map(cmd -> jda.upsertCommand(cmd.getData()))
		// 	.forEach(cmd -> cmd.queue());
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if(event.getUser().isBot()) return;
		
		AtelierUser user = db.getUser(event.getUser().getIdLong());
		log.info(user + " used command: " + event.getCommandString());
		reg.getCommand(event.getName()).execute(user, event);
	}
	
	@Override
	public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
		if(event.getUser().isBot()) return;
		
		AtelierUser user = db.getUser(event.getUser().getIdLong());
		reg.getCommand(event.getName()).complete(user, event);
	}

	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		Menu menu = menus.getMenu(event.getMessageIdLong());
		if(menu != null) menu.onButtonInteract(event);
	}
	
	@Override
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
		Menu menu = menus.getMenu(event.getMessageIdLong());
		if(menu != null) menu.onSelectInteract(event);
	}
}
