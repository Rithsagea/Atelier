package com.atelier.console.commands;

import java.util.Comparator;
import java.util.UUID;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleGroupCommand;
import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.ConsoleCache;
import com.atelier.database.AtelierDB;
import com.atelier.discord.AtelierUser;
import com.atelier.dnd.character.AtelierCharacter;

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
			logger.info(getMessage("info").addUser(user).get());
			
			cache.selectUser(user);
		}
		
	}
	
	private class UserAddCharacter extends BaseConsoleSubcommand {
		public UserAddCharacter() {
			super("addcharacter");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			AtelierUser selectedUser = cache.selectedUser();
			AtelierCharacter character = AtelierDB.getInstance().getCharacter(UUID.fromString(args[2]));
			
			selectedUser.addCharacter(character);
			logger.info(getMessage("info")
				.addCharacter(character)
				.addUser(selectedUser)
				.get());
		}
	}
	
	private class UserRemoveCharacter extends BaseConsoleSubcommand {
		public UserRemoveCharacter() {
			super("removecharacter");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			AtelierUser selectedUser = cache.selectedUser();
			AtelierCharacter character = AtelierDB.getInstance().getCharacter(UUID.fromString(args[2]));

			if(selectedUser.removeCharacter(character))
				logger.info(getMessage("info")
						.addCharacter(character)
						.addUser(selectedUser)
						.get());
			else
				logger.info(getMessage("missing")
						.addCharacter(character)
						.addUser(selectedUser)
						.get());
		}
	}
	
	private class UserListCharacter extends BaseConsoleSubcommand {
		public UserListCharacter() {
			super("listcharacter");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			AtelierUser selectedUser = cache.selectedUser();

			logger.info(getMessage("info").addUser(selectedUser).get());
			selectedUser.getCharacters().forEach(
				(AtelierCharacter character) -> logger.info(character.toString()));
		}
	}
	
	private ConsoleCache cache;

	public UserConsoleCommand(ConsoleCache cache) {
		super("user");
		this.cache = cache;

		registerSubcommand(new UserList());
		registerSubcommand(new UserSelect());
		registerSubcommand(new UserAddCharacter());
		registerSubcommand(new UserRemoveCharacter());
		registerSubcommand(new UserListCharacter());
	}
	
	@Override
	public void executeDefault(String[] args, Logger logger) {
		AtelierUser selectedUser = cache.selectedUser();
		
		logger.info("Name: " + selectedUser.getName());
		logger.info("Id: " + selectedUser.getId());
	}
}
