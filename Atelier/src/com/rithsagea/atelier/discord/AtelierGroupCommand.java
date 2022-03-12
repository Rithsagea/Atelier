package com.rithsagea.atelier.discord;

import java.util.List;

import com.rithsagea.atelier.dnd.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AtelierGroupCommand extends CommandRegistry implements AtelierCommand {

	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		if(args.size() >= 2) {
			String label = args.get(1);
			AtelierCommand command = getCommand(label);
			
			if(command != null && command.getLevel().ordinal() <= user.getLevel().ordinal()) {
				args.remove(0);
				command.execute(user, args, event);
				return;
			}
		}
		
		executeDefault(user, args, event);
	}
	
	public abstract void executeDefault(User user, List<String> args, MessageReceivedEvent event);

}
