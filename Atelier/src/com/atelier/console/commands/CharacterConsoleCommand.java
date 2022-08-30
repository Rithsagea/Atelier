package com.atelier.console.commands;

import java.util.Comparator;
import java.util.UUID;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleGroupCommand;
import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.database.AtelierDB;
import com.atelier.dnd.AtelierCharacter;

public class CharacterConsoleCommand extends BaseConsoleGroupCommand {
	
	private class CharacterList extends BaseConsoleSubcommand {
		public CharacterList() {
			super("list");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			logger.info("Characters:");
			AtelierDB.getInstance().listCharacters()
				.stream()
				.sorted(new Comparator<AtelierCharacter>() {
					@Override
					public int compare(AtelierCharacter u1, AtelierCharacter u2) {
						return u1.getName().compareTo(u2.getName());
					}
				}).forEach(
					(AtelierCharacter c) -> logger.info("{} [{}]", c.getName(), c.getId()));
		}
	}
	
	private class CharacterSelect extends BaseConsoleSubcommand {

		public CharacterSelect() {
			super("select");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			AtelierCharacter character = AtelierDB.getInstance().getCharacter(UUID.fromString(args[2]));
			logger.info(getMessage("info").addCharacter(character).get());
			
			selectedCharacter = character;
		}
	}
	
	private class CharacterNew extends BaseConsoleSubcommand {
		public CharacterNew() {
			super("new");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			AtelierCharacter character = new AtelierCharacter();
			AtelierDB.getInstance().addCharacter(character);
			logger.info(getMessage("info").addCharacter(character).get());
		}
	}
	
	private class CharacterName extends BaseConsoleSubcommand {

		public CharacterName() {
			super("name");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			if(args.length == 2) {
				logger.info(getMessage("info").addCharacter(selectedCharacter).get());
			} else {
				logger.info(getMessage("set")
						.addCharacter(selectedCharacter)
						.add("name", args[2]).get());
				selectedCharacter.setName(args[2]);
			}
		}
		
	}
	
	private static AtelierCharacter selectedCharacter;
	
	public CharacterConsoleCommand() {
		super("character", "char");
		
		registerSubcommand(new CharacterList());
		registerSubcommand(new CharacterSelect());
		registerSubcommand(new CharacterNew());
		registerSubcommand(new CharacterName());
	}

	@Override
	public void executeDefault(String[] args, Logger logger) {
		if(selectedCharacter == null) {
			logger.info(getMessage("missing").get());
			return;
		}
		
		logger.info("Name: " + selectedCharacter.getName());
		logger.info("Id: " + selectedCharacter.getId());
	}
}
