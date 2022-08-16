package com.atelier.console.commands;

import java.util.Comparator;
import java.util.UUID;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.BaseGroupConsoleCommand;
import com.atelier.database.AtelierDB;
import com.atelier.dnd.AtelierCharacter;

public class CharacterConsoleCommand extends BaseGroupConsoleCommand {
	
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
			logger.info("Selected Character: {}", character);
			
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
			logger.info("Created new character with id: " + character.getId());
		}
	}
	
	private static AtelierCharacter selectedCharacter;
	
	public CharacterConsoleCommand() {
		super("character", "char");
		
		registerSubcommand(new CharacterList());
		registerSubcommand(new CharacterSelect());
		registerSubcommand(new CharacterNew());
	}

	@Override
	public void executeDefault(String[] args, Logger logger) {
		if(selectedCharacter == null) {
			logger.info("No character selected!");
			return;
		}
		
		logger.info("Name: " + selectedCharacter.getName());
		logger.info("Id: " + selectedCharacter.getId());
	}
}
