package com.atelier.console.commands;

import java.util.Comparator;
import java.util.UUID;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.BaseConsoleGroupCommand;
import com.atelier.database.AtelierDB;
import com.atelier.discord.AtelierUser;
import com.atelier.dnd.AtelierCharacter;

public class UserConsoleCommand extends BaseConsoleGroupCommand {

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
	
	private class UserAddCharacter extends BaseConsoleSubcommand {
		public UserAddCharacter() {
			super("addcharacter");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			AtelierCharacter character = AtelierDB.getInstance().getCharacter(UUID.fromString(args[2]));
			selectedUser.addCharacter(character);
			logger.info("Added {} to {}", character, selectedUser);
		}
	}
	
	private class UserRemoveCharacter extends BaseConsoleSubcommand {
		public UserRemoveCharacter() {
			super("removecharacter");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			AtelierCharacter character = AtelierDB.getInstance().getCharacter(UUID.fromString(args[2]));
			if(selectedUser.removeCharacter(character))
				logger.info("Removed {} from {}", character, selectedUser);
			else
				logger.info("{} does not have {}", selectedUser, character);
		}
	}
	
	private class UserListCharacter extends BaseConsoleSubcommand {
		public UserListCharacter() {
			super("listcharacter");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			logger.info("{} Characters: ", selectedUser);
			selectedUser.getCharacters().forEach(
				(AtelierCharacter character) -> logger.info(character.toString()));
		}
	}
	
	private static AtelierUser selectedUser;
	
	public UserConsoleCommand() {
		super("user");
		
		registerSubcommand(new UserList());
		registerSubcommand(new UserSelect());
		registerSubcommand(new UserAddCharacter());
		registerSubcommand(new UserRemoveCharacter());
		registerSubcommand(new UserListCharacter());
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
