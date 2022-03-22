package com.rithsagea.atelier.dnd.discord.character;

import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.discord.CharacterSubCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class CharacterRollCommand extends CharacterSubCommand {

	public CharacterRollCommand(AtelierBot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
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
	public void execute(User user, Sheet sheet, List<String> args, MessageReceivedEvent event) {
		event.getMessage().reply("Select value to roll For")
			.setActionRows(
					ActionRow.of(
							Button.of(ButtonStyle.PRIMARY, "dice", "Dice"),
							Button.of(ButtonStyle.PRIMARY, "savingThrow", "Saving Throw"),
							Button.of(ButtonStyle.PRIMARY, "abilityCheck", "Ability Check"),
							Button.of(ButtonStyle.PRIMARY, "skillCheck", "Skill Check")))
			.queue();
	}
	
}
