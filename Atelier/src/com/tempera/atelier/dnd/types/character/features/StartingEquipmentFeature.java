package com.tempera.atelier.dnd.types.character.features;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.character.Attribute;
import com.tempera.atelier.dnd.types.equipment.Item;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

@IndexedItem("feature-starting-equipment")
public class StartingEquipmentFeature implements Attribute {

	public class EquipmentOption {
		private String name;
		private Item item;

		public EquipmentOption(String name, Item item) {
			this.name = name;
			this.item = item;
		}

		public String getName() {
			return name;
		}

		public Item getItem() {
			return item;
		}
	}

	private List<List<EquipmentOption>> options;

	@SafeVarargs
	public StartingEquipmentFeature(List<EquipmentOption>... options) {
		this.options = Stream.of(options)
			.map(Collections::unmodifiableList)
			.collect(Collectors.toList());
	}

	@Override
	public String getName() {
		return "Starting Equipment";
	}

	@Override
	public Menu getMenu() {
		return new StartingEquipmentMenu();
	}

	private class StartingEquipmentMenu implements Menu {
		@Override
		public Message initialize() {
			MessageBuilder mb = new MessageBuilder();
			EmbedBuilder eb = new EmbedBuilder();

			eb.setTitle("Starting Equipment");
			eb.appendDescription(
				"You start with the following items, plus anything provided by your background.");
			eb.appendDescription("\n\n");
			eb.appendDescription("Lorem Ipsum");

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
}
