package com.atelier.discord.commands.character;

import com.atelier.discord.commands.BaseInteraction.GroupCommand;

public class CharacterCommand extends GroupCommand {

	public CharacterCommand() {
		registerSubcommand(new CharacterListCommand());
		registerSubcommand(new CharacterInfoCommand());
		registerSubcommand(new CharacterSelectCommand());
		
		registerSubcommand(new CharacterAttributeCommand());
		registerSubcommand(new CharacterInventoryCommand());
		registerSubcommandGroup(new CharacterRollCommand());
	}
}
