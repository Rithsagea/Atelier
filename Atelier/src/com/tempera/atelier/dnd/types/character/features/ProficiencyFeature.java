package com.tempera.atelier.dnd.types.character.features;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.rithsagea.util.choice.Choice;
import com.rithsagea.util.event.EventHandler;
import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.dnd.IndexedItem;
import com.tempera.atelier.dnd.events.LoadProficiencyEvent.LoadEquipmentProficiencyEvent;
import com.tempera.atelier.dnd.events.LoadProficiencyEvent.LoadSavingProficiencyEvent;
import com.tempera.atelier.dnd.events.LoadProficiencyEvent.LoadSkillProficiencyEvent;
import com.tempera.atelier.dnd.types.character.Attribute;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Equipment;
import com.tempera.atelier.dnd.types.enums.Proficiency;
import com.tempera.atelier.dnd.types.enums.Skill;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

@IndexedItem("feature-proficiencies")
public class ProficiencyFeature implements Attribute {
	private class ProficiencyMessageBuilder extends MessageBuilder {
		public ProficiencyMessageBuilder() {
			EmbedBuilder embed = new EmbedBuilder();
			StringBuilder content = new StringBuilder();
			SelectMenu.Builder select = SelectMenu.create("skills")
					.setRequiredRange(0, skillChoices.getCount())
					.setPlaceholder(String.format("Choose %d skills...", skillChoices.getCount()));
			String prefix = "";
			
			embed.setTitle("**" + getName() + "**");
			embed.setColor(Color.BLUE);
			
			for(Ability ability : savingProficiencies) {
				content.append(prefix);
				content.append(ability);
				prefix = ", ";
			}
			embed.addField("**Saving**", content.toString(), true);
			
			content.setLength(0);
			content.append(String.format("Choose %d from: ", skillChoices.getCount()));
			prefix = "";
			for(int x = 0; x < skillChoices.getChoices().size(); x++) {
				content.append(prefix);
				content.append(String.format(skillChoices.isSelected(x) ? "__%s__" : "%s", skillChoices.getChoice(x)));
				prefix = ", ";
				select.addOption(skillChoices.getChoices().get(x).toString(), Integer.toString(x));
			}
			embed.addField("**Skills**", content.toString(), true);
			
			select.setDefaultValues(skillChoices.getSelected()
					.stream()
					.map(s -> Integer.toString(s))
					.collect(Collectors.toList()));
			
			content.setLength(0);
			prefix = "";
			for(Equipment equipment : equipmentProficiencies) {
				content.append(prefix);
				content.append(equipment);
				prefix = ", ";
			}
			embed.addField("**Equipment**", content.toString(), false);
			
			setEmbeds(embed.build());
			setActionRows(ActionRow.of(select.build()));
		}
	}
	
	private class ProficiencyMenu extends Menu {

		@Override
		public Message initialize() {
			return new ProficiencyMessageBuilder().build();
		}

		@Override
		public void onButtonInteract(ButtonInteractionEvent event) { }

		@Override
		public void onSelectInteract(SelectMenuInteractionEvent event) {
			skillChoices.clear();
			for(SelectOption option : event.getSelectedOptions()) {
				skillChoices.selectChoice(Integer.parseInt(option.getValue()));
			}
			
			event.editMessage(new ProficiencyMessageBuilder().build()).queue();
		}
	}
	
	private Set<Ability> savingProficiencies;
	private Set<Skill> skillProficiencies;
	private Set<Equipment> equipmentProficiencies;
	
	private Choice<Skill> skillChoices;
	
	public ProficiencyFeature() {
		skillChoices = new Choice<>();
		
		savingProficiencies = new HashSet<>();
		skillProficiencies = new HashSet<>();
		equipmentProficiencies = new HashSet<>();
	}
	
	public ProficiencyFeature(Choice<Skill> skills, Proficiency... proficiencies) {
		this();
	
		skillChoices = skills;
		
		for(Proficiency p : proficiencies) {
			if(p instanceof Ability) savingProficiencies.add((Ability) p);
			if(p instanceof Skill) skillProficiencies.add((Skill) p);
			if(p instanceof Equipment) equipmentProficiencies.add((Equipment) p);
		}
	}
	
	@Override
	public String getName() {
		return "Proficiencies";
	}
	
	@EventHandler
	private void onLoadSavingProficiency(LoadSavingProficiencyEvent e) {
		for(Ability ability : savingProficiencies) e.addProficiency(ability);
	}
	
	@EventHandler
	private void onLoadSkillProficiency(LoadSkillProficiencyEvent e) {
		for(Skill skill : skillProficiencies) e.addProficiency(skill);
	}
	
	@EventHandler
	private void onLoadEquipmentProficiency(LoadEquipmentProficiencyEvent e) {
		for(Equipment equip : equipmentProficiencies) e.addProficiency(equip);
	}

	@Override
	public Menu getMenu() {
		return new ProficiencyMenu();
	}
}
