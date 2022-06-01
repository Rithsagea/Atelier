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
import com.tempera.atelier.discord.commands.AtelierCommand;
import com.tempera.atelier.discord.commands.CommandRegistry;
import com.tempera.atelier.discord.commands.StopCommand;
import com.tempera.atelier.discord.commands.WaifuCommand;
import com.tempera.atelier.discord.music.MusicCommand;
import com.tempera.atelier.dnd.commands.CharacterCommand;
import com.tempera.atelier.dnd.commands.edit.EditCommand;
import com.tempera.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	
	private AtelierDB db;
	private CommandRegistry reg;
	private Config config;
	private Logger logger;
	private MenuManager menuManager;
	
	public MessageListener(AtelierBot bot) {
		db = bot.getDatabase();
		reg = bot.getCommandRegistry();
		config = bot.getConfig();
		logger = bot.getLogger();
		menuManager = bot.getMenuManager();
		
		registerCommands(bot);
	}
	
	private Map<String, AtelierCommand> macroMap;
	
	private void registerCommands(AtelierBot bot) {
		AtelierCommand musicCommand = new MusicCommand(bot);
		AtelierCommand characterCommand = new CharacterCommand(bot);
		AtelierCommand waifuCommand = new WaifuCommand(bot);
		AtelierCommand editCommand = new EditCommand(bot);
		
		macroMap = new HashMap<>();
		
		macroMap.put("w", waifuCommand);
		macroMap.put("m", musicCommand);
		macroMap.put("d", characterCommand);
		macroMap.put("e", editCommand);
		
		reg.registerCommand(new StopCommand(bot));
		reg.registerCommand(waifuCommand);
		reg.registerCommand(musicCommand);
		reg.registerCommand(characterCommand);
		reg.registerCommand(editCommand);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();
		net.dv8tion.jda.api.entities.User author = event.getAuthor();
		
		User user = db.getUser(author.getIdLong());
		user.setName(author.getName());
		
		//Command Logic goes below
		if(event.getAuthor().isBot()) return;
		if(message.startsWith(config.getCommandPrefix())) {
			
			List<String> args = new ArrayList<String>(Arrays.asList(
					message.substring(config.getCommandPrefix().length()).split(" ")));
			
			AtelierCommand command = reg.getCommand(args.get(0));
			if(command != null && command.getLevel().compareTo(user.getLevel()) <= 0) {
				logger.info("Received Command: " + command.getLabel() + " from " + author);
				command.execute(user, Collections.unmodifiableList(args), event);
			}
		}
		
		for(Entry<String, AtelierCommand> cmd : macroMap.entrySet()) {
			if(message.startsWith(cmd.getKey() + config.getCommandPrefix())) {
				List<String> args = new ArrayList<String>(Arrays.asList(
						message.substring(config.getCommandPrefix().length() + cmd.getKey().length())
						.split(" ")));
				AtelierCommand command = cmd.getValue();
				if(command.getLevel().compareTo(user.getLevel()) > 0) return;
				
				args.add(0, command.getLabel());
				logger.info("Received Macro: " + command.getLabel() + " from " + author);
				command.execute(user, Collections.unmodifiableList(args), event);
			}
		}
	}
	
	
	//if the menu doesn't exist just nuke the components
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		Menu m = menuManager.getMenu(event.getMessageIdLong());
		if(m != null) {
			m.onButtonInteract(event);
		} else {
			event.getMessage().editMessageComponents().queue();
		}
	}
	
	@Override
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
		Menu m = menuManager.getMenu(event.getMessageIdLong());
		if(m != null) {
			m.onSelectInteract(event);
		} else {
			event.getMessage().editMessageComponents().queue();
		}
	}
}
