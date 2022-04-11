package com.rithsagea.atelier.dnd.discord.character;

import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.Ability;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.discord.CharacterSubCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterRollCommand extends CharacterSubCommand {

	public CharacterRollCommand(AtelierBot bot) {
		super(bot);
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

	/**
	 * str
	 * int
	 * cha
	 * wis
	 * con
	 * dex
	 */
	@Override
	public void execute(User user, Sheet sheet, List<String> args, MessageReceivedEvent event) {
		Ability a = null;
		String s = args.get(2);
		switch(args.get(2))
		{
		case "str":
			sheet.getAbilityScore(Ability.STRENGTH);
			a = Ability.STRENGTH;
			break;
		case "int":
			sheet.getAbilityScore(Ability.INTELLIGENCE);
			a = Ability.INTELLIGENCE;
			break;
		case "cha":
			sheet.getAbilityScore(Ability.CHARISMA);
			a = Ability.CHARISMA;
			break;
		case "wis":
			sheet.getAbilityScore(Ability.WISDOM);
			a = Ability.WISDOM;
			break;
		case "con":
			sheet.getAbilityScore(Ability.CONSTITUTION);
			a = Ability.CONSTITUTION;
			break;
		case "dex":
			sheet.getAbilityScore(Ability.DEXTERITY);
			a = Ability.DEXTERITY;
			break;
		}
		event.getChannel().sendMessage(s + " roll: " + (int)(Math.random()*20 + 1 + sheet.getAbilityModifier(a))).queue();
		
	}
	
}
