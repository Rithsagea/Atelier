package com.tempera.atelier.discord.acommands;

import java.util.List;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class GroupCommand extends BaseCommand {

	private CommandRegistry registry;
	
	public GroupCommand(String label, List<String> aliases, PermissionLevel level) {
		super(label, aliases, level);
		registry = new CommandRegistry();
	}

	protected CommandRegistry getCommandRegistry() {
		return registry;
	}

	@Override
	public void execute(User user, List<String> args,
		MessageReceivedEvent event) {
		if (args.size() > 1) {
			AtelierCommand cmd = registry.getCommand(args.get(1)); // 2nd argument should be subcommand
			if (cmd != null) {
				cmd.execute(user, args.subList(1, args.size()), event); // don't include previous argument
				return;
			}
		}

		executeDefault(user, args, event);
	}

	public abstract void executeDefault(User user, List<String> args,
		MessageReceivedEvent event);
}
