package com.atelier.discord;

import java.util.stream.Stream;

import org.slf4j.Logger;

import com.atelier.AtelierBot;
import com.atelier.Config;
import com.atelier.discord.commands.AbstractInteraction.AbstractCommand;
import com.atelier.discord.commands.CommandRegistry;
import com.atelier.discord.commands.StopCommand;
import com.atelier.discord.commands.WaifuCommand;
import com.atelier.discord.commands.character.CharacterCommand;
import com.atelier.discord.commands.music.MusicCommand;
import com.atelier.dnd.commands.campaign.CampaignCommand;
import com.atelier.dnd.types.AtelierDB;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	private CommandRegistry reg = new CommandRegistry();
	
	private AtelierBot bot;
	private AtelierDB db = AtelierDB.getInstance();
	private MenuManager menus = MenuManager.getInstance();
	
	private Logger log;
	
	public CommandListener(AtelierBot bot) {
		this.bot = bot;
		this.log = bot.getLogger();
		registerCommands();
	}
	
	public CommandRegistry getRegistry() {
		return reg;
	}
	
	
	private AbstractCommand waifuCommand;
	private AbstractCommand musicCommand;
	private AbstractCommand stopCommand;
	private AbstractCommand characterCommand;
	private AbstractCommand campaignCommand;
	
	private void registerCommands() {
		waifuCommand = new WaifuCommand();
		musicCommand  = new MusicCommand();
		stopCommand = new StopCommand(bot);
		characterCommand = new CharacterCommand();
		campaignCommand = new CampaignCommand();
		
		reg.registerCommand(waifuCommand);
		reg.registerCommand(musicCommand);
		reg.registerCommand(stopCommand);
		reg.registerCommand(characterCommand);
		reg.registerCommand(campaignCommand);
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		//testing commands
		Guild guild = event.getJDA().getGuildById(Config.getInstance().getTestingGuildId());
//		guild.retrieveCommands().queue(list -> list.forEach(cmd -> cmd.delete().queue()));
		Stream.of(stopCommand, characterCommand, campaignCommand)
			.map(cmd -> guild.upsertCommand(cmd.getData()))
			.forEach(cmd -> cmd.queue());
		
		//global commands
		JDA jda = event.getJDA();
//		jda.retrieveCommands().queue(list -> list.forEach(cmd -> cmd.delete().queue()));
		Stream.of(waifuCommand, musicCommand)
			.map(cmd -> jda.upsertCommand(cmd.getData()))
			.forEach(cmd -> cmd.queue());
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if(event.getUser().isBot()) return;
		
		net.dv8tion.jda.api.entities.User author = event.getUser();
		User user = db.getUser(author.getIdLong());
		user.setName(author.getName());
		
		log.info(user + " used command: " + event.getCommandString());
		
		reg.getCommand(event.getName()).execute(user, event);
	}
	
	@Override
	public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
		if(event.getUser().isBot()) return;
		
		net.dv8tion.jda.api.entities.User author = event.getUser();
		User user = db.getUser(author.getIdLong());
		user.setName(author.getName());
		
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
