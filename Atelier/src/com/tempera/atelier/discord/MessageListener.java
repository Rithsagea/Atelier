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
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.commands.CharacterCommand;
import com.tempera.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	
	private AtelierDB db;
	private CommandRegistry reg;
	private Config config;
	private Logger logger;
	
	public MessageListener(AtelierBot bot) {
		db = bot.getDatabase();
		reg = bot.getCommandRegistry();
		config = bot.getConfig();
		logger = bot.getLogger();
		
		registerCommands(bot);
	}
	
	private Map<String, AtelierCommand> macroMap;
	
	private void registerCommands(AtelierBot bot) {
		AtelierCommand musicCommand = new MusicCommand(bot);
		AtelierCommand characterCommand = new CharacterCommand(bot);
		AtelierCommand waifuCommand = new WaifuCommand();
		
		macroMap = new HashMap<>();
		
		macroMap.put("w", waifuCommand);
		macroMap.put("m", musicCommand);
		macroMap.put("d", characterCommand);
		
		reg.registerCommand(new StopCommand(bot));
		reg.registerCommand(waifuCommand);
		reg.registerCommand(musicCommand);
		reg.registerCommand(characterCommand);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();
		net.dv8tion.jda.api.entities.User author = event.getAuthor();
		
		User user = db.getUser(author.getIdLong());
		user.setName(author.getName());
		
		if(message.startsWith(config.getCommandPrefix())) {
			
			List<String> args = new ArrayList<String>(Arrays.asList(
					message.substring(config.getCommandPrefix().length()).split(" ")));
			
			AtelierCommand command = reg.getCommand(args.get(0));
			if(command != null && command.getLevel().ordinal() <= user.getLevel().ordinal()) {
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
				args.add(0, command.getLabel());
				logger.info("Received Macro: " + command.getLabel() + " from " + author);
				command.execute(user, Collections.unmodifiableList(args), event);
			}
		}
	}
	
//	@Override
//	public void onButtonInteraction(ButtonInteractionEvent event) {
//		AtelierMenu menu = menuManager.getMenu(event.getMessageIdLong());
//		if(menu != null) menu.onButtonInteract(event);
//		else {
//			event.getMessage().editMessageComponents().queue();
//		}
//	}
//	
//	@Override
//	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
//		AtelierMenu menu = menuManager.getMenu(event.getMessageIdLong());
//		if(menu != null) menu.onMenuSelect(event);
//		else {
//			event.getMessage().editMessageComponents().queue();
//		}
//	}
}
