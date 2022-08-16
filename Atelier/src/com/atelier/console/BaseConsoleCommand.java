package com.atelier.console;

public abstract class BaseConsoleCommand implements AbstractConsoleCommand {

	private final String label;
	private final String[] aliases;
	
	public BaseConsoleCommand(String label, String... aliases) {
		this.label = label;
		this.aliases = aliases;
	}
	
	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String[] getAliases() {
		return aliases;
	}
}
