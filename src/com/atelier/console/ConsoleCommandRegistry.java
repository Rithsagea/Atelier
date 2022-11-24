package com.atelier.console;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ConsoleCommandRegistry {
	private Map<String, AbstractConsoleCommand> commands = new HashMap<>();
	
	public void registerCommand(AbstractConsoleCommand cmd) {
		commands.put(cmd.getLabel(), cmd);
		Stream.of(cmd.getAliases()).forEach(str -> commands.put(str, cmd));
	}
	
	public AbstractConsoleCommand getCommand(String label) {
		return commands.get(label);
	}
}
