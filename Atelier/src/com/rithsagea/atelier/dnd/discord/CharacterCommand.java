package com.rithsagea.atelier.dnd.discord;

import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.AtelierSubCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterCommand extends AtelierSubCommand {
	
	public CharacterCommand(AtelierBot bot) {
		registerCommand(new CharacterCreateCommand(bot));
		registerCommand(new CharacterListCommand(bot));
		registerCommand(new CharacterSelectCommand(bot));
	}
	
	@Override
	public String getLabel() {
		return "character";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] {"char", "dnd"});
	}

	@Override
	public void executeDefault(List<String> args, MessageReceivedEvent event) {
		
	}

}
