package com.atelier.console.commands;

import java.util.Comparator;
import java.util.UUID;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleGroupCommand;
import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.ConsoleCache;
import com.atelier.database.AtelierDB;
import com.atelier.dnd.Ability;
import com.atelier.dnd.AbilitySpread;
import com.atelier.dnd.character.AtelierCharacter;
import com.atelier.util.WordUtil;

public class CharacterConsoleCommand extends BaseConsoleGroupCommand {
	
	private class CharacterList extends BaseConsoleSubcommand {
		public CharacterList() {
			super("list");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			logger.info(getMessage("info").get());
			AtelierDB.getInstance().listCharacters()
				.stream()
				.sorted(new Comparator<AtelierCharacter>() {
					@Override
					public int compare(AtelierCharacter u1, AtelierCharacter u2) {
						if(!u1.getName().equals(u2.getName()))
							return u1.getName().compareTo(u2.getName());
						return u1.getId().compareTo(u2.getId());
					}
				}).forEach(c -> logger.info(c.toString()));
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
			
			cache.selectedCharacter = character;
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
				logger.info(getMessage("info").addCharacter(cache.selectedCharacter).get());
			} else {
				logger.info(getMessage("set")
						.addCharacter(cache.selectedCharacter)
						.add("name", args[2]).get());
				cache.selectedCharacter.setName(args[2]);
			}
		}
		
	}
	
	private class CharacterAbility extends BaseConsoleSubcommand {
		public CharacterAbility() {
			super("ability");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			AbilitySpread spread = cache.selectedCharacter.getAbilitySpread();

			if(args.length == 2) {
				logger.info(getMessage("info.points").add("points", spread.getPoints()).get());
				for(Ability ability : Ability.values()) {
					logger.info(getMessage("info.score")
							.addAbility(ability)
							.add("baseScore", cache.selectedCharacter.getBaseScore(ability))
							.add("abilityScore", cache.selectedCharacter.getAbilityScore(ability))
							.add("abilityModifier", WordUtil.formatModifier(cache.selectedCharacter.getAbilityModifier(ability)))
							.get());
				}
			} else {
				Ability ability = Ability.fromLabel(args[2].toLowerCase());
				int score = Integer.parseInt(args[3]);
				
				spread.setScore(ability, score);
				logger.info(getMessage("set")
						.addAbility(ability)
						.add("score", score)
						.get());
			}
		}
	}
	
	private ConsoleCache cache;

	public CharacterConsoleCommand(ConsoleCache cache) {
		super("character", "char");
		
		this.cache = cache;

		registerSubcommand(new CharacterList());
		registerSubcommand(new CharacterSelect());
		registerSubcommand(new CharacterNew());
		registerSubcommand(new CharacterName());
		registerSubcommand(new CharacterAbility());
	}

	@Override
	public void executeDefault(String[] args, Logger logger) {
		if(cache.selectedCharacter == null) {
			logger.info(getMessage("missing").get());
			return;
		}
		
		logger.info("Name: " + cache.selectedCharacter.getName());
		logger.info("Id: " + cache.selectedCharacter.getId());
	}
}
