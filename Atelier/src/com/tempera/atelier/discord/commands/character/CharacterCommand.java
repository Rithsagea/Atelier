package com.tempera.atelier.discord.commands.character;

import com.tempera.atelier.discord.commands.SlashGroupCommand;

public class CharacterCommand extends SlashGroupCommand {

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
