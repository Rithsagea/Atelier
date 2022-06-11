package com.tempera.atelier.dnd.acommands.character;

import java.util.List;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.aaagarbage.MenuManager;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.acommands.PermissionLevel;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterInventoryCommand extends CharacterBaseCommand {

	private MenuManager menuManager;

	public CharacterInventoryCommand(AtelierBot bot) {
		super(bot, "inventory", DataUtil.list("i"), PermissionLevel.USER);

		menuManager = bot.getMenuManager();
	}

	@Override
	public void execute(Sheet sheet, User user, List<String> args,
		MessageReceivedEvent event) {
		menuManager.addMenu(event.getChannel(),
			new CharacterInventoryMenu(sheet, menuManager));
	}

}
