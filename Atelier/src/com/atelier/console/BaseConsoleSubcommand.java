package com.atelier.console;

public abstract class BaseConsoleSubcommand extends BaseConsoleCommand implements AbstractConsoleSubcommand {

	public BaseConsoleSubcommand(String label, String... aliases) {
		super(label, aliases);
	}

}
