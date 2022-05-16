package com.tempera.atelier.dnd.commands;

import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Skill;
import com.tempera.util.WordUtil;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu.Builder;

public class CharacterMenu extends Menu{

	private String selected = "";
	private Sheet sheet;
	public CharacterMenu(Message message , Sheet sheet) {
		super(message);
		this.sheet = sheet;
		Builder menu = SelectMenu.create("menu:roll")
				.setPlaceholder("Choose what to roll for")
				.setRequiredRange(1, 1);
				for(Ability a : Ability.values())
					menu.addOption(WordUtil.capitalize(a.name().replace("_", " ")), a.getLabel());
				for(Skill s : Skill.values())
					menu.addOption(WordUtil.capitalize(s.name().replace("_", " ")), s.getLabel());
				SelectMenu m = menu.build();
				message.editMessageComponents(
						ActionRow.of(m),
						ActionRow.of(Button.primary("get", "Get")))
					.queue();
	}

	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {
		String s = CharacterRollCommand.calculate(selected, sheet);
		event.reply(s).queue();
	}

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) {
		selected = event.getSelectedOptions().get(0).getValue();
		event.deferEdit().queue();
		
	}

}
