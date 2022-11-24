package com.atelier.console;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

public abstract class BaseConsoleGroupCommand extends BaseConsoleCommand implements AbstractConsoleGroupCommand {

	private Map<String, AbstractConsoleSubcommand> subcommands = new HashMap<>();
	
	public BaseConsoleGroupCommand(String label, String... aliases) {
		super(label, aliases);
	}
	
	public void registerSubcommand(AbstractConsoleSubcommand cmd) {
		subcommands.put(cmd.getLabel(), cmd);
		for(String alias : cmd.getAliases()) {
			subcommands.put(alias, cmd);
		}
	}
	
	@Override
	public final void execute(String[] args, Logger logger) {
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
