package com.atelier.discord.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.atelier.discord.commands.AbstractInteraction.AbstractCommand;

public class CommandRegistry {
	private Map<String, AbstractCommand> commands = new HashMap<>();
	
	public void registerCommand(AbstractCommand cmd) {
		addCommand(cmd.getName(), cmd);
	}
	
	private void addCommand(String label, AbstractCommand cmd) {
		if(!commands.containsKey(label))
			commands.put(label, cmd);
	}
	
	public AbstractCommand getCommand(String label) {
		return commands.get(label);
	}
	
	public Collection<AbstractCommand> getCommands() {
		return commands.values();
	}
}
