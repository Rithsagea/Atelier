package com.tempera.atelier.dnd.acommands.character;

import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.dnd.types.Sheet;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Skill;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu.Builder;

public class CharacterRollMenu implements Menu {

	private String selected = "";
	private Sheet sheet;

	public CharacterRollMenu(MessageChannel channel, Sheet sheet) {
		this.sheet = sheet;
	}

	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {
		event.reply(CharacterRollCommand.roll(selected, sheet))
			.queue();
	}

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) {
		selected = event.getSelectedOptions()
			.get(0)
			.getValue();
		event.deferEdit()
			.queue();
	}

	@Override
	public Message initialize() {
		MessageBuilder res = new MessageBuilder("Choose a stat to roll for:");
		Builder menu = SelectMenu.create("roll")
			.setPlaceholder("Choose a stat...")
			.setRequiredRange(1, 1);

		for (Ability a : Ability.values())
			menu.addOption(a.toString(), a.getLabel());
		for (Skill s : Skill.values())
			menu.addOption(s.toString(), s.getLabel());

		res.setActionRows(ActionRow.of(menu.build()),
			ActionRow.of(Button.primary("get", "Get")));

		return res.build();
	}

}
