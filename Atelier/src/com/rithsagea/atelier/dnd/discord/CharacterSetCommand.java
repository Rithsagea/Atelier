package com.rithsagea.atelier.dnd.discord;

import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;

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
		switch(args.get(1)) {
		
		case "name":
			sheet.setName(args.get(2));
			event.getChannel().sendMessage("Changed name to: " + args.get(2)).queue();
			break;
			
		}
	}

}
