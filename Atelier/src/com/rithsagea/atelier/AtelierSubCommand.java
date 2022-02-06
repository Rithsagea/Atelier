package com.rithsagea.atelier;

import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AtelierSubCommand extends CommandRegistry implements AtelierCommand {

	@Override
	public void execute(List<String> args, MessageReceivedEvent event) {
		if(args.size() >= 2) {
			String label = args.get(1);
			AtelierCommand command = getCommand(label);
			
			if(command != null) {
				args.remove(0);
				command.execute(args, event);
				return;
			}
		}
		
		executeDefault(args, event);
	}
	
	public abstract void executeDefault(List<String> args, MessageReceivedEvent event);

}
