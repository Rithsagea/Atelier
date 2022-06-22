package com.atelier.discord.commands.character;

import java.util.List;

import com.atelier.discord.Menu;
import com.atelier.discord.MenuManager;
import com.atelier.dnd.types.Sheet;
import com.atelier.dnd.types.equipment.Item;
import com.atelier.util.ColorUtil;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu.Builder;

public class CharacterInventoryMenu implements Menu {

	private Sheet sheet;
	
	private String selected;

	public CharacterInventoryMenu(Sheet sheet) {
		this.sheet = sheet;
	}

	@Override
	public Message initialize() {
		MessageBuilder mb = new MessageBuilder();
		EmbedBuilder eb = new EmbedBuilder();
		int number = 0;

		eb.setColor(ColorUtil.BROWN);
		eb.setTitle("Inventory");
		List<Item> items = sheet.getInventory()
			.getContents();
		for (Item item : items) {
			eb.appendDescription("[" + number +"] " + item + "\n");
			number++;
		}

		mb.setEmbeds(eb.build());
		Builder menu = SelectMenu.create("menu:roll").setPlaceholder("Choose an item").setRequiredRange(1, 1);
		for (int i = 0; i < items.size(); i++)
			menu.addOption("[" + i + "] "+ items.get(i), "" + i);
		mb.setActionRows(ActionRow.of(menu.build()), ActionRow.of(Button.primary("get", "Get")));
		return mb.build();
	}

	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {
		Item item = sheet.getInventory().getContents().get(Integer.parseInt(selected));
		MenuManager.getInstance().addMenu(item.getMenu(), true, event);
	}

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) {
		event.deferEdit().queue();
		selected = event.getSelectedOptions().get(0).getValue();
	}

}
