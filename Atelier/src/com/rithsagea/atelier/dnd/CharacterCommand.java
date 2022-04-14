package com.rithsagea.atelier.dnd;

import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.commands.AtelierCommand;
import com.rithsagea.atelier.discord.commands.PermissionLevel;
import com.rithsagea.atelier.dnd.database.AtelierDB;
import com.rithsagea.util.WordUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterCommand implements AtelierCommand {
	
	private AtelierDB db;
	
	public CharacterCommand(AtelierBot bot) {
		db = bot.getDatabase();
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
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		Sheet sheet = db.getSheet(user.getSheetId());
		
		if(args.size() <= 1) {
			StringBuilder builder = new StringBuilder();
			builder.append("Name: " + sheet.getName() + "\n");
			event.getChannel().sendMessage(builder.toString()).queue();
			
			return;
		}
		
		switch(args.get(1)) {
		
		case "roll":
		Ability a = null;
		switch(args.get(2))
		{
			case "str": a = Ability.STRENGTH; break;
			case "int": a = Ability.INTELLIGENCE; break;
			case "cha": a = Ability.CHARISMA; break;
			case "wis": a = Ability.WISDOM; break;
			case "con": a = Ability.CONSTITUTION; break;
			case "dex":	a = Ability.DEXTERITY; break;
		}
		int roll = (int) (Math.random() * 20 + 1 + sheet.getAbilityModifier(a));
		event.getChannel().sendMessage(
				WordUtil.capitalize(a.name()) + " roll: " + roll).queue();
		break;
		
		
		
		}
	}

}
