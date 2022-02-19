package com.rithsagea.atelier.dnd.discord;

import java.util.List;
import java.util.Set;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.AtelierCommand;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterListCommand implements AtelierCommand {

	private AtelierDB db;
	
	public CharacterListCommand(AtelierBot bot) {
		db = bot.getDatabase();
	}
	
	@Override
	public String getLabel() {
		return "list";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public void execute(List<String> args, MessageReceivedEvent event) {
		Set<Sheet> sheets = db.listSheets();
		
		StringBuilder builder = new StringBuilder();
		for(Sheet sheet : sheets) {
			builder.append(sheet.getId());
			builder.append("\n");
		}
		
		event.getChannel().sendMessage(builder.toString()).queue();
	}

}
