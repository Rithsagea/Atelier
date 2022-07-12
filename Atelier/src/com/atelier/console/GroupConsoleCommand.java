package com.atelier.console;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;

public abstract class GroupConsoleCommand extends BaseConsoleCommand {

	private Map<String, AbstractConsoleSubcommand> subcommands = new HashMap<>();
	
	public GroupConsoleCommand(String label, String... aliases) {
		super(label, aliases);
	}
	
	protected void registerSubcommand(AbstractConsoleSubcommand cmd) {
		subcommands.put(cmd.getLabel(), cmd);
		Stream.of(cmd.getAliases()).forEach(str -> subcommands.put(str, cmd));
	}

	@Override
	public void execute(String[] args, Logger logger) {
		if(args.length > 1) {
			AbstractConsoleSubcommand cmd = subcommands.get(args[1]);
			if(cmd != null) {
				cmd.execute(args, logger);
				return;
			}
		}
		
		executeDefault(args, logger);
	}
	
	public abstract void executeDefault(String[] args, Logger logger);
	
}
