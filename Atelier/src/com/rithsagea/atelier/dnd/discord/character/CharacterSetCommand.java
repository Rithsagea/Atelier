package com.rithsagea.atelier.dnd.discord.character;

import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.discord.CharacterSubCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterSetCommand extends CharacterSubCommand {

	public CharacterSetCommand(AtelierBot bot) {
		super(bot);		
	}
	
	@Override
	public String getLabel() {
		return "set";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.ADMINISTRATOR;
	}
	
	@Override
	public void execute(User user, Sheet sheet, List<String> args, MessageReceivedEvent event) {
		switch(args.get(2)) {
		
		case "name":
			sheet.setName(args.get(3));
			event.getChannel().sendMessage("Changed name to: " + args.get(3)).queue();
			break;
			
		}
	}

}
