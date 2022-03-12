package com.rithsagea.atelier.discord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.Config;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	
	private AtelierDB db;
	private CommandRegistry reg;
	private Config config;
	
	public MessageListener(AtelierBot bot) {
		db = bot.getDatabase();
		reg = bot.getCommandRegistry();
		config = bot.getConfig();
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
			if(command != null && command.getLevel().ordinal() <= user.getLevel().ordinal()) command.execute(user, args, event);
		}
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		
	}
	
	@Override
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
		
	}
}
