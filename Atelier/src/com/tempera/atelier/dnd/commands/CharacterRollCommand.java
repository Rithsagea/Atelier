package com.tempera.atelier.dnd.commands;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Skill;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterRollCommand extends CharacterSubCommand{

	private MenuManager menuManager;
	
	public CharacterRollCommand(AtelierBot bot) {
		super(bot.getDatabase());
		menuManager = bot.getMenuManager();
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
		
		if(args.size() == 1) {
			menuManager.addMenu(event.getChannel(), new CharacterMenu(event.getChannel(), sheet));
			return;
		}
		
		event.getChannel().sendMessage(roll(args.get(1), sheet)).queue();
	}
	
	public static String roll(String stat, Sheet sheet)
	{
		Ability a = Ability.fromLabel(stat);
		if(a != null) {
			int roll = (int) (Math.random() * 20 + 1) + sheet.getAbilityModifier(a);
			return String.format("%s roll: %d", a, roll);
		}
		
		Skill s = Skill.fromLabel(stat);
		if(s != null) {
			int roll = (int) (Math.random() * 20 + 1) + sheet.getSkillModifier(s);
			return String.format("%s roll: %d", s, roll);
		}
		
		return "Invalid Stat!";
	}
}
