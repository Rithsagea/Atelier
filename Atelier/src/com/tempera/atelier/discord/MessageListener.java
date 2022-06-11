package com.tempera.atelier.discord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.Config;
import com.tempera.atelier.discord.commands.SlashCommandRegistry;
import com.tempera.atelier.discord.commands.SlashWaifuCommand;
import com.tempera.atelier.discord.commands.StopCommand;
import com.tempera.atelier.discord.lcommands.AtelierCommand;
import com.tempera.atelier.discord.lcommands.CommandRegistry;
import com.tempera.atelier.discord.music.MusicCommand;
import com.tempera.atelier.dnd.commands.campaign.CampaignCommand;
import com.tempera.atelier.dnd.commands.character.CharacterCommand;
import com.tempera.atelier.dnd.commands.edit.EditCommand;
import com.tempera.atelier.dnd.types.AtelierDB;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class MessageListener extends ListenerAdapter {

	private AtelierDB db;
	private CommandRegistry toBeDeletedCommandRegistry;
	private Config config;
	private Logger logger;
	private MenuManager menuManager;
	
	private SlashCommandRegistry reg = new SlashCommandRegistry();
	
	private JDA jda;

	public MessageListener(AtelierBot bot) {
		db = bot.getDatabase();
		toBeDeletedCommandRegistry = bot.getCommandRegistry();
		config = bot.getConfig();
		logger = bot.getLogger();
		menuManager = bot.getMenuManager();
		
		jda = bot.getJda();
		
		registerCommands(bot);
	}

	private Map<String, AtelierCommand> macroMap;

	private void registerCommands(AtelierBot bot) {
		reg.registerCommand(new SlashWaifuCommand());
		reg.registerCommand(new StopCommand(bot));
		
		AtelierCommand musicCommand = new MusicCommand(bot);
		AtelierCommand characterCommand = new CharacterCommand(bot);
		AtelierCommand campaignCommand = new CampaignCommand(bot);
		AtelierCommand editCommand = new EditCommand(bot);

		macroMap = new HashMap<>();

		macroMap.put("m", musicCommand);
		macroMap.put("d", characterCommand);
		macroMap.put("c", campaignCommand);
		macroMap.put("e", editCommand);

		toBeDeletedCommandRegistry.registerCommand(musicCommand);
		toBeDeletedCommandRegistry.registerCommand(characterCommand);
		toBeDeletedCommandRegistry.registerCommand(campaignCommand);
		toBeDeletedCommandRegistry.registerCommand(editCommand);
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		Guild guild = jda.getGuildById(config.getTestingGuildId());
		reg.getCommands().stream()
			.map(cmd->cmd.getData())
			.map(CommandData::fromData)
			.map(guild::upsertCommand)
			.forEach(a -> a.queue());
		
		//TODO global commands here
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if(event.getUser().isBot()) return;
		
		net.dv8tion.jda.api.entities.User author = event.getUser();
		User user = db.getUser(author.getIdLong());
		user.setName(author.getName());
		
		reg.getCommand(event.getName()).execute(user, event);
	}
	
	@Override
	public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
		if(event.getUser().isBot()) return;
		
		net.dv8tion.jda.api.entities.User author = event.getUser();
		User user = db.getUser(author.getIdLong());
		user.setName(author.getName());
		
		reg.getCommand(event.getName()).complete(event);
	}

	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();
		net.dv8tion.jda.api.entities.User author = event.getAuthor();

		User user = db.getUser(author.getIdLong());
		user.setName(author.getName());

		// Command Logic goes below
		if (event.getAuthor().isBot())
			return;
		if (message.startsWith(config.getCommandPrefix())) {

			List<String> args = new ArrayList<String>(Arrays.asList(message
				.substring(config.getCommandPrefix().length()).split(" ")));

			AtelierCommand command = toBeDeletedCommandRegistry.getCommand(args.get(0));
			if (command != null && command.getLevel()
				.compareTo(user.getLevel()) <= 0) {
				logger.info("Received Command: " + command.getLabel() + " from " + author);
				command.execute(user, Collections.unmodifiableList(args), event);
			}
		}

		for (Entry<String, AtelierCommand> cmd : macroMap.entrySet()) {
			if (message.startsWith(cmd.getKey() + config.getCommandPrefix())) {
				List<String> args = new ArrayList<String>(Arrays.asList(message
					.substring(config.getCommandPrefix().length()
						+ cmd.getKey().length()).split(" ")));
				AtelierCommand command = cmd.getValue();
				if (command.getLevel()
					.compareTo(user.getLevel()) > 0)
					return;

				args.add(0, command.getLabel());
				logger.info("Received Macro: " + command.getLabel() + " from "
					+ author);
				command.execute(user, Collections.unmodifiableList(args),
					event);
			}
		}
	}

	// if the menu doesn't exist just nuke the components

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		Menu m = menuManager.getMenu(event.getMessageIdLong());
		if (m != null) {
			m.onButtonInteract(event);
		} else {
			event.getMessage()
				.editMessageComponents()
				.queue();
		}
	}

	@Override
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
		Menu m = menuManager.getMenu(event.getMessageIdLong());
		if (m != null) {
			m.onSelectInteract(event);
		} else {
			event.getMessage()
				.editMessageComponents()
				.queue();
		}
	}
}
