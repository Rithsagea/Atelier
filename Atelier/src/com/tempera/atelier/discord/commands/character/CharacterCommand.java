package com.tempera.atelier.discord.commands.character;

import com.tempera.atelier.discord.commands.GroupCommand;

public class CharacterCommand extends GroupCommand {

	public CharacterCommand() {
		super("character", "General utility command for player characters");
		
		registerSubcommand(new CharacterListCommand());
		registerSubcommand(new CharacterInfoCommand());
		registerSubcommand(new CharacterSelectCommand());
		
		registerSubcommand(new CharacterAttributeCommand());
		registerSubcommand(new CharacterInventoryCommand());
		registerSubcommandGroup(new CharacterRollCommand());
	}
}
