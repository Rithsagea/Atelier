package com.tempera.atelier.dnd.commands;

import java.util.Map.Entry;

import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.dnd.types.Sheet;
import com.tempera.atelier.dnd.types.character.Attribute;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu.Builder;

public class CharacterAttributeMenu implements Menu {

	private String selected;
	private Sheet sheet;
	
	private MenuManager menuManager;
	
	public CharacterAttributeMenu(Sheet sheet, MenuManager menuManager) {
		this.sheet = sheet;
		this.menuManager = menuManager;
	}

	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {
		event.deferEdit().queue();
		menuManager.addMenu(event.getChannel(), sheet.getAttribute(selected).getMenu());
	}

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) {
		selected = event.getSelectedOptions().get(0).getValue();
		event.deferEdit().queue();
	}

	@Override
	public Message initialize() {
		MessageBuilder res = new MessageBuilder("Choose an attribute");
		Builder menu = SelectMenu.create("menu:roll")
				.setPlaceholder("Choose attribute")
				.setRequiredRange(1, 1);
				for(Entry<String, Attribute> entry : sheet.getAttributeMap().entrySet())
					menu.addOption(entry.getValue().getName(), entry.getKey());
				res.setActionRows(
						ActionRow.of(menu.build()),
						ActionRow.of(Button.primary("get", "Get")));
		return res.build();
	}
	

}
