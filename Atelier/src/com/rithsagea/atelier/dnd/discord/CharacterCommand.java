package com.rithsagea.atelier.dnd.discord;

import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.AtelierSubCommand;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterCommand extends AtelierSubCommand {
	
	private AtelierDB db;
	
	public CharacterCommand(AtelierBot bot) {
		db = bot.getDatabase();
		
		registerCommand(new CharacterCreateCommand(bot));
		registerCommand(new CharacterSelectCommand(bot));
		registerCommand(new CharacterSetCommand(bot));
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
		User user = db.getUser(event.getAuthor().getIdLong());
		Sheet sheet = db.getSheet(user.getSheetId());
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("Name: " + sheet.getName() + "\n");
		
		event.getChannel().sendMessage(builder.toString()).queue();
	}

}
