package com.tempera.atelier.dnd.commands;

import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterAttributeCommand extends CharacterSubCommand{

	private MenuManager menuManager;
	
	public CharacterAttributeCommand(AtelierBot bot) {
		super(bot.getDatabase());
		
		menuManager = bot.getMenuManager();
	}

	@Override
	public String getLabel() {
		return "attribute";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("a");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(Sheet sheet, User user, List<String> args, MessageReceivedEvent event) {
		menuManager.addMenu(event.getChannel(), new CharacterAttributeMenu(sheet, menuManager));
	}

}
