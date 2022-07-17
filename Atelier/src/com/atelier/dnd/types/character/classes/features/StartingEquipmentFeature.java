package com.atelier.dnd.types.character.classes.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.atelier.database.IndexedItem;
import com.atelier.discord.Menu;
import com.atelier.dnd.events.LoadEvent.LoadSheetEvent;
import com.atelier.dnd.types.Sheet;
import com.atelier.dnd.types.character.CharacterAttribute;
import com.atelier.dnd.types.equipment.Inventory;
import com.atelier.dnd.types.equipment.Item;
import com.atelier.util.ColorUtil;
import com.atelier.util.WordUtil;
import com.rithsagea.util.DataUtil;
import com.rithsagea.util.event.EventHandler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu.Builder;

@IndexedItem("feature-starting-equipment")
public class StartingEquipmentFeature implements CharacterAttribute {

	public static class EquipmentOption {
		private String name;
		private List<Item> items;

		public EquipmentOption() { }

		public EquipmentOption(String name, Item... items) {
			this.name = name;
			this.items = Arrays.asList(items);
		}

		public String getName() {
			return name;
		}

		public List<Item> getItems() {
			return items;
		}
	}

	private List<Integer> selectedOptions;
	private List<List<EquipmentOption>> options;
	private boolean isClaimed = false;

	private transient Sheet sheet;
	
	public StartingEquipmentFeature() {
		this.selectedOptions = Collections.emptyList();
		this.options = Collections.emptyList();
		isClaimed = false;
	}

	@SafeVarargs
	public StartingEquipmentFeature(List<EquipmentOption>... options) {
		this.options = Stream.of(options)
			.map(Collections::unmodifiableList)
			.collect(Collectors.toList());

		this.selectedOptions = new ArrayList<>(
			Collections.nCopies(options.length, -1));
		for (int x = 0; x < options.length; x++) {
			if (options[x].size() <= 1)
				this.selectedOptions.set(x, 0);
		}
	}

	@Override
	public String getName() {
		return "Starting Equipment";
	}

	@Override
	public Menu getMenu() {
		return new StartingEquipmentMenu();
	}

	@EventHandler
	private void onLoadSheet(LoadSheetEvent event) {
		this.sheet = event.getSheet();
	}
	
	private class StartingEquipmentMessageBuilder extends MessageBuilder {
		public StartingEquipmentMessageBuilder() {
			EmbedBuilder eb = new EmbedBuilder();

			eb.setColor(ColorUtil.BROWN);
			eb.setTitle("Starting Equipment");
			eb.appendDescription("You start with the following items, plus anything provided by your background.");
			eb.appendDescription("\n\n");

			List<ActionRow> actionRows = new ArrayList<>();
			for (int i = 0; i < options.size(); i++) {
				List<EquipmentOption> option = options.get(i);

				eb.appendDescription(WordUtil.BULLET_POINT);
				eb.appendDescription("\t");
				if (option.size() == 1) {
					eb.appendDescription(String.format("__%s__", option.get(0).getName()));
				} else {
					String prefix = "";
					for (int j = 0; j < option.size(); j++) {
						eb.appendDescription(prefix);
						eb.appendDescription(String.format(selectedOptions.get(i) == j 
							? "__(%c) %s__" : "(%c) %s",
							'a' + j, option.get(j).getName()));
						prefix = " or ";
					}
				}
				eb.appendDescription("\n");

				if (options.get(i)
					.size() <= 1)
					continue;
				Builder sb = SelectMenu.create(Integer.toString(i));

				for (int j = 0; j < options.get(i).size(); j++) {
					sb.addOption(String.format("(%c) %s", 'a' + j,
						options.get(i).get(j).getName()),
						Integer.toString(j));
				}
				if (selectedOptions.get(i) != -1)
					sb.setDefaultValues(DataUtil.list(Integer.toString(selectedOptions.get(i))));

				actionRows.add(ActionRow.of(sb.build()));
			}
			actionRows.add(ActionRow.of(
				isClaimed ? Button.primary("claim", "Claim").asDisabled() :
					Button.primary("claim", "Claim").asEnabled()));

			setEmbeds(eb.build());
			setActionRows(actionRows);

		}
	}

	private class StartingEquipmentMenu implements Menu {
		@Override
		public Message initialize() {
			return new StartingEquipmentMessageBuilder().build();
		}

		@Override
		public void onButtonInteract(ButtonInteractionEvent event) {
			for(int selected : selectedOptions) {
				if(selected == -1) {
					event.reply("Please select equipment for each option").setEphemeral(true).queue();
					return;
				}
			}
			
			if(!isClaimed) {
				isClaimed = true;
				Inventory inventory = sheet.getInventory();
				for(int x = 0; x < options.size(); x++) {
					options.get(x).get(selectedOptions.get(x)).getItems().forEach(inventory::addItem);
				}
				
				event.reply("Succesfully claimed starting equipment!").setEphemeral(true).queue();
			}
			
			event.reply("Starting equipment has already been claimed!").setEphemeral(true).queue();
		}

		@Override
		public void onSelectInteract(SelectMenuInteractionEvent event) {
			selectedOptions.set(Integer.parseInt(event.getComponentId()),
				Integer.parseInt(event.getSelectedOptions().get(0).getValue()));
			
			event.editMessage(new StartingEquipmentMessageBuilder().build()).queue();
		}
	}
}
