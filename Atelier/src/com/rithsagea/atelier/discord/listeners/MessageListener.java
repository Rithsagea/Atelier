package com.rithsagea.atelier.discord.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.Config;
import com.rithsagea.atelier.discord.AtelierCommand;
import com.rithsagea.atelier.discord.CommandRegistry;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	
	private CommandRegistry reg;
	private Config config;
	
	public MessageListener(AtelierBot bot) {
		reg = bot.getCommandRegistry();
		config = bot.getConfig();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();
		if(message.startsWith(config.getCommandPrefix())) {
			
			List<String> args = new ArrayList<String>(Arrays.asList(
					message.substring(config.getCommandPrefix().length()).split(" ")));
			
			AtelierCommand command = reg.getCommand(args.get(0));
			if(command != null) command.execute(args, event);
		}
	}
}
