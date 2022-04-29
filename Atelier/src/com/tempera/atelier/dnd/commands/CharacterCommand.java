package com.tempera.atelier.dnd.commands;

import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.commands.AtelierCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.database.AtelierDB;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Skill;
import com.tempera.util.WordUtil;

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
			for(Ability ability : Ability.values()) {
				builder.append(String.format("%s: %d [%s]\n", 
						WordUtil.capitalize(ability.name()),
						sheet.getAbilityScore(ability),
						WordUtil.formatModifier(sheet.getAbilityModifier(ability))));
			}
			
			event.getChannel().sendMessage(builder.toString()).queue();
			
			return;
		}
		
		switch(args.get(1)) {
		
		case "roll":
		for(Ability a : Ability.values())
		{
			if(args.get(2).equals(a.getLabel()))
				{
				int roll = (int) (Math.random() * 20 + 1) + sheet.getAbilityModifier(a);
				event.getChannel().sendMessage(WordUtil.capitalize(a.name().replace("_", " ")) + " roll: " + roll).queue();
				return;
				}
		}
		for(Skill s : Skill.values())
		{
			if(args.get(2).equals(s.getLabel()))
				{
				int roll = (int) (Math.random() * 20 + 1) + sheet.getAbilityModifier(s.getAbility());
				event.getChannel().sendMessage(WordUtil.capitalize(s.name().replace("_", " ")) + " roll: " + roll).queue();
				return;
				}
		}
	}
	}
}
