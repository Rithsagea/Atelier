package com.atelier.console.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.BaseGroupConsoleCommand;
import com.atelier.database.AtelierDB;
import com.atelier.discord.User;

public class UserConsoleCommand extends BaseGroupConsoleCommand {

	private class UserList extends BaseConsoleSubcommand {

		public UserList() {
			super("list");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			List<User> users = new ArrayList<>(AtelierDB.getInstance().listUsers());
			Collections.sort(users, new Comparator<User>() {
				@Override
				public int compare(User u1, User u2) {
					return u1.getName().compareTo(u2.getName());
				}
			});
			
			logger.info("Users: ");
			for(User user : users) {
				logger.info("{} [{}]", user.getName(), user.getId());
			}
		}
		
	}
	
	private class UserSelect extends BaseConsoleSubcommand {

		public UserSelect() {
			super("select");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			User user = AtelierDB.getInstance().getUser(Long.parseLong(args[2]));
			logger.info("Selected User: {}", user);
			
			selectedUser = user;
		}
		
	}
	
	private static User selectedUser;
	
	public UserConsoleCommand() {
		super("user");
		
		registerCommand(new UserList());
		registerCommand(new UserSelect());
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
