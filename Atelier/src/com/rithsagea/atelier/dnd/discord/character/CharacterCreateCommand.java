package com.rithsagea.atelier.dnd.discord.character;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.AtelierCommand;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterCreateCommand implements AtelierCommand {

	private AtelierDB db;
	
	public CharacterCreateCommand(AtelierBot bot) {
		db = bot.getDatabase();
	}
	
	@Override
	public String getLabel() {
		return "create";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("new");
	}

	
	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.ADMINISTRATOR;
	}
	
	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		Sheet sheet = args.size() > 1 ?
				new Sheet(UUID.fromString(args.get(1))) : new Sheet();
		sheet.setName("Unnamed");
		db.addSheet(sheet);
		
		event.getChannel().sendMessage("Created new character sheet: " + sheet.getId()).queue();
	}
	
}
