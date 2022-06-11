package com.tempera.atelier.discord.lcommands;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

	private Map<String, AtelierCommand> commands;

	public CommandRegistry() {
		commands = new HashMap<>();
	}

	public void registerCommand(AtelierCommand command) {
		addCommand(command.getLabel(), command);
		if (command.getAliases() != null)
			command.getAliases()
				.forEach(alias -> addCommand(alias, command));
	}

	private void addCommand(String label, AtelierCommand command) {
		if (!commands.containsKey(label))
			commands.put(label, command);
	}

	public AtelierCommand getCommand(String label) {
		return commands.get(label);
	}
}
