package com.rithsagea.atelier.dnd.discord.character;

import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.discord.menu.AtelierMenuManager;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.discord.CharacterSubCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterRollCommand extends CharacterSubCommand {

	private AtelierMenuManager menuManager;
	
	public CharacterRollCommand(AtelierBot bot) {
		super(bot);
		
		menuManager = bot.getMenuManager();
	}

	@Override
	public String getLabel() {
		return "roll";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(User user, Sheet sheet, List<String> args, MessageReceivedEvent event) {
		new RollMenu(menuManager, event.getMessage().reply(" "));
	}
	
}
