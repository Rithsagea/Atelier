package com.atelier.console.commands;

import java.util.Comparator;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.BaseGroupConsoleCommand;
import com.atelier.database.AtelierDB;
import com.atelier.discord.AtelierUser;

public class UserConsoleCommand extends BaseGroupConsoleCommand {

	private class UserList extends BaseConsoleSubcommand {

		public UserList() {
			super("list");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			logger.info("Users:");
			AtelierDB.getInstance().listUsers()
				.stream()
				.sorted(new Comparator<AtelierUser>() {
					@Override
					public int compare(AtelierUser u1, AtelierUser u2) {
						return u1.getName().compareTo(u2.getName());
					}
				}).forEach(
					(AtelierUser c) -> logger.info("{} [{}]", c.getName(), c.getId()));
		}
		
	}
	
	private class UserSelect extends BaseConsoleSubcommand {

		public UserSelect() {
			super("select");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			AtelierUser user = AtelierDB.getInstance().getUser(Long.parseLong(args[2]));
			logger.info("Selected User: {}", user);
			
			selectedUser = user;
		}
		
	}
	
	private static AtelierUser selectedUser;
	
	public UserConsoleCommand() {
		super("user");
		
		registerSubcommand(new UserList());
		registerSubcommand(new UserSelect());
	}
	
	@Override
	public void executeDefault(String[] args, Logger logger) {
		if(selectedUser == null) {
			logger.info("No user selected!");
			return;
		}
		
		logger.info("Name: " + selectedUser.getName());
		logger.info("Id: " + selectedUser.getId());
	}
}
