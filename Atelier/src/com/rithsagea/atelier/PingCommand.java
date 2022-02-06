package com.rithsagea.atelier;

import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand implements AtelierCommand {

	@Override
	public String getLabel() {
		return "ping";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public void execute(List<String> args, MessageReceivedEvent event) {
		event.getChannel().sendMessage("Pong!").submit();
	}
}
