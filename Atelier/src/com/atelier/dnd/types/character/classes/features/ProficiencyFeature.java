package com.atelier.dnd.types.character.classes.features;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.atelier.database.Subtype;
import com.atelier.discord.Menu;
import com.atelier.dnd.events.LoadProficiencyEvent.LoadEquipmentProficiencyEvent;
import com.atelier.dnd.events.LoadProficiencyEvent.LoadSavingProficiencyEvent;
import com.atelier.dnd.events.LoadProficiencyEvent.LoadSkillProficiencyEvent;
import com.atelier.dnd.types.character.CharacterAttribute;
import com.atelier.dnd.types.enums.Ability;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Proficiency;
import com.atelier.dnd.types.enums.Skill;
import com.rithsagea.util.choice.Choice;
import com.rithsagea.util.event.EventHandler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

@Subtype("feature-proficiencies")
public class ProficiencyFeature implements CharacterAttribute {
	private Set<Ability> savingProficiencies;
	private Set<EquipmentType> equipmentProficiencies;

	private Choice<Skill> skillChoices;

	public ProficiencyFeature() {
		skillChoices = new Choice<>();

		savingProficiencies = new HashSet<>();
		equipmentProficiencies = new HashSet<>();
	}

	public ProficiencyFeature(Choice<Skill> skills,
		Proficiency... proficiencies) {
		this();

		skillChoices = skills;

		for (Proficiency p : proficiencies) {
			if (p instanceof Ability)
				savingProficiencies.add((Ability) p);
			if (p instanceof EquipmentType)
				equipmentProficiencies.add((EquipmentType) p);
		}
	}

	@Override
	public String getName() {
		return "Proficiencies";
	}

	@EventHandler
	private void onLoadSavingProficiency(LoadSavingProficiencyEvent e) {
		savingProficiencies.forEach(e::addProficiency);
	}

	@EventHandler
	private void onLoadSkillProficiency(LoadSkillProficiencyEvent e) {
		skillChoices.getChoices().forEach(e::addProficiency);
	}

	@EventHandler
	private void onLoadEquipmentProficiency(LoadEquipmentProficiencyEvent e) {
		equipmentProficiencies.forEach(e::addProficiency);
	}

	@Override
	public Menu getMenu() {
		return new ProficiencyMenu();
	}

	private class ProficiencyMessageBuilder extends MessageBuilder {
		public ProficiencyMessageBuilder() {
			EmbedBuilder embed = new EmbedBuilder();
			StringBuilder content = new StringBuilder();
			SelectMenu.Builder select = SelectMenu.create("skills")
				.setRequiredRange(0, skillChoices.getCount())
				.setPlaceholder(String.format("Choose %d skills...",
					skillChoices.getCount()));
			String prefix = "";

			embed.setTitle("**" + getName() + "**");
			embed.setColor(Color.BLUE);

			for (Ability ability : savingProficiencies) {
				content.append(prefix);
				content.append(ability);
				prefix = ", ";
			}
			embed.addField("**Saving**", content.toString(), true);

			content.setLength(0);
			content.append(String.format("Choose %d from: ", skillChoices.getCount()));
			prefix = "";
			for (int x = 0; x < skillChoices.getChoices()
				.size(); x++) {
				content.append(prefix);
				content.append(String.format(skillChoices.isSelected(x) ? "__%s__" : "%s",
						skillChoices.getChoice(x)));
				prefix = ", ";
				select.addOption(skillChoices.getChoices()
					.get(x)
					.toString(), Integer.toString(x));
			}
			embed.addField("**Skills**", content.toString(), true);

			select.setDefaultValues(skillChoices.getSelected()
				.stream()
				.map(s -> Integer.toString(s))
				.collect(Collectors.toList()));

			content.setLength(0);
			prefix = "";
			for (EquipmentType equipment : equipmentProficiencies) {
				content.append(prefix);
				content.append(equipment);
				prefix = ", ";
			}
			embed.addField("**Equipment**", content.toString(), false);

			setEmbeds(embed.build());
			setActionRows(ActionRow.of(select.build()));
		}
	}

	private class ProficiencyMenu implements Menu {

		@Override
		public Message initialize() {
			return new ProficiencyMessageBuilder().build();
		}

		@Override
		public void onButtonInteract(ButtonInteractionEvent event) {
		}

		@Override
		public void onSelectInteract(SelectMenuInteractionEvent event) {
			skillChoices.clear();
			for (SelectOption option : event.getSelectedOptions()) {
				skillChoices.selectChoice(Integer.parseInt(option.getValue()));
			}
			
			event.editMessage(new ProficiencyMessageBuilder().build()).queue();
		}
	}

}
