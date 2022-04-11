package com.rithsagea.atelier.discord;

import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.dnd.User;

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
		return Arrays.asList("quit", "exit");
	}
	
	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.ADMINISTRATOR;
	}

	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		event.getChannel().sendMessage("Stopping AtelierBot!").queue(
				(m) -> {bot.stop();});
	}
}
