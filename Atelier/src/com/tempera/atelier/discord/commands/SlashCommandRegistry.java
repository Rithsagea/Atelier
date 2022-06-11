package com.tempera.atelier.discord.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SlashCommandRegistry {
	private Map<String, SlashAbstractCommand> commands = new HashMap<>();
	
	public void registerCommand(SlashAbstractCommand cmd) {
		addCommand(cmd.getName(), cmd);
	}
	
	private void addCommand(String label, SlashAbstractCommand cmd) {
		if(!commands.containsKey(label))
			commands.put(label, cmd);
	}
	
	public SlashAbstractCommand getCommand(String label) {
		return commands.get(label);
	}
	
	public Collection<SlashAbstractCommand> getCommands() {
		return commands.values();
	}
}
