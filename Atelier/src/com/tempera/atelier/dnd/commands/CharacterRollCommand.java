package com.tempera.atelier.dnd.commands;

import java.util.List;

import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.database.AtelierDB;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Skill;
import com.tempera.util.WordUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterRollCommand extends CharacterSubCommand{

	public CharacterRollCommand(AtelierDB db) {
		super(db);
	}

	@Override
	public String getLabel() {
		return "roll";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(Sheet sheet, User user, List<String> args, MessageReceivedEvent event) {
		for(Ability a : Ability.values())
		{
			if(args.get(1).equals(a.getLabel()))
				{
				int roll = (int) (Math.random() * 20 + 1) + sheet.getAbilityModifier(a);
				event.getChannel().sendMessage(WordUtil.capitalize(a.name().replace("_", " ")) + " roll: " + roll).queue();
				return;
				}
		}
		for(Skill s : Skill.values())
		{
			if(args.get(1).equals(s.getLabel()))
				{
				int roll = (int) (Math.random() * 20 + 1) + sheet.getAbilityModifier(s.getAbility());
				event.getChannel().sendMessage(WordUtil.capitalize(s.name().replace("_", " ")) + " roll: " + roll).queue();
				return;
				}
		}
	}	
}
