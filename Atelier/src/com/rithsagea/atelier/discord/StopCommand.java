package com.rithsagea.atelier.discord;

import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.AtelierBot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StopCommand implements AtelierCommand {

	private AtelierBot bot;
	
	public StopCommand(AtelierBot bot) {
		this.bot = bot;
	}
	
	@Override
	public String getLabel() {
		return "stop";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] {"quit", "exit"});
	}

	@Override
	public void execute(List<String> args, MessageReceivedEvent event) {
		event.getChannel().sendMessage("Stopping AtelierBot!").queue();
		
		bot.stop();
	}

}
