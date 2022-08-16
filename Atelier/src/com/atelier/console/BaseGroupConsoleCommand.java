package com.atelier.console;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

public abstract class BaseGroupConsoleCommand extends BaseConsoleCommand {

	private Map<String, AbstractConsoleSubcommand> subcommands = new HashMap<>();
	
	public BaseGroupConsoleCommand(String label, String... aliases) {
		super(label, aliases);
	}
	
	protected void registerCommand(AbstractConsoleSubcommand cmd) {
		subcommands.put(cmd.getLabel(), cmd);
		for(String alias : cmd.getAliases()) {
			subcommands.put(alias, cmd);
		}
	}
	
	@Override
	public void execute(String[] args, Logger logger) {
		if(args.length >= 2) {
			AbstractConsoleSubcommand cmd = subcommands.get(args[1]);
			if(cmd != null) {
				cmd.execute(args, logger);
				return;
			}
		}
		
		executeDefault(args, logger);
	}
	
	public void executeDefault(String[] args, Logger logger) { }

}
