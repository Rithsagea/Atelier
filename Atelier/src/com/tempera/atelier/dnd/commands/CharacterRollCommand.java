package com.tempera.atelier.dnd.commands;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.discord.commands.WaifuMenu;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.database.AtelierDB;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Skill;
import com.tempera.util.WordUtil;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu.Builder;

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
		
		if(args.size() == 1)
		{
			event.getChannel().sendMessage("Please select a valid input")
			.queue((Message message) -> {
				menuManager.addMenu(message.getIdLong(), new CharacterMenu(message, sheet));
			});
			return;
		}
		String s = calculate(args.get(1), sheet);
		if(s != null)
		{
			event.getChannel().sendMessage(s).queue();
			return;
		}
		
		event.getChannel().sendMessage("Invalid input").queue((Message message) -> {
			menuManager.addMenu(message.getIdLong(), new CharacterMenu(message, sheet));
		});
		return;
	}
	
	public static String calculate(String string, Sheet sheet)
	{
		for(Ability a : Ability.values())
		{
			if(string.equals(a.getLabel()))
				{
				int roll = (int) (Math.random() * 20 + 1) + sheet.getAbilityModifier(a);
				return WordUtil.capitalize(a.name().replace("_", " ")) + " roll: " + roll;
				}
		}
		for(Skill s : Skill.values())
		{
			if(string.equals(s.getLabel()))
				{
				int roll = (int) (Math.random() * 20 + 1) + sheet.getAbilityModifier(s.getAbility());
				return WordUtil.capitalize(s.name().replace("_", " ")) + " roll: " + roll;
				}
		}
		return null;
	}
}
