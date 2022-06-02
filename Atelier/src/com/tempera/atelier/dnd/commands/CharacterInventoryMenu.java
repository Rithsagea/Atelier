package com.tempera.atelier.dnd.commands;

import java.awt.Color;
import java.util.List;

import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.dnd.types.Sheet;
import com.tempera.atelier.dnd.types.equipment.Item;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public class CharacterInventoryMenu implements Menu {

	private Sheet sheet;

	public CharacterInventoryMenu(Sheet sheet) {
		this.sheet = sheet;
	}

	@Override
	public Message initialize() {
		MessageBuilder mb = new MessageBuilder();
		EmbedBuilder eb = new EmbedBuilder();

		eb.setColor(new Color(165, 42, 42));
		eb.setTitle("Inventory");
		List<Item> items = sheet.getInventory()
			.getContents();
		for (Item item : items) {
			eb.appendDescription(item.getName() + "\n");
		}

		mb.setEmbeds(eb.build());
		return mb.build();
	}

	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {

	}

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) {

	}

}
