package com.rithsagea.atelier.discord;

import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface AtelierCommand {
	public String getLabel();
	public List<String> getAliases();
	
	public void execute(List<String> args, MessageReceivedEvent event);
}
