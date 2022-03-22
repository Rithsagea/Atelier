package com.rithsagea.atelier.dnd.discord;

import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.AtelierGroupCommand;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.database.AtelierDB;
import com.rithsagea.atelier.dnd.discord.character.CharacterCreateCommand;
import com.rithsagea.atelier.dnd.discord.character.CharacterRollCommand;
import com.rithsagea.atelier.dnd.discord.character.CharacterSelectCommand;
import com.rithsagea.atelier.dnd.discord.character.CharacterSetCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterCommand extends AtelierGroupCommand {
	
	private AtelierDB db;
	
	public CharacterCommand(AtelierBot bot) {
		db = bot.getDatabase();
		
		registerCommand(new CharacterCreateCommand(bot));
		registerCommand(new CharacterSelectCommand(bot));
		registerCommand(new CharacterSetCommand(bot));
		registerCommand(new CharacterRollCommand(bot));
	}
	
	@Override
	public String getLabel() {
		return "character";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("char", "dnd");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}
	
	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		Sheet sheet = db.getSheet(user.getSheetId());
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("Name: " + sheet.getName() + "\n");
		
		event.getChannel().sendMessage(builder.toString()).queue();
	}

}
