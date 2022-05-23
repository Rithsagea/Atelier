package com.tempera.atelier.dnd.types.character.features;

import java.util.HashSet;
import java.util.Set;

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

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

@IndexedItem("feature-proficiencies")
public class ProficiencyFeature implements Attribute {
	private class ProficiencyMenu extends Menu {

		@Override
		public MessageAction initialize(MessageChannel channel) {
			return channel.sendMessage("dummytext");
		}

		@Override
		public void onButtonInteract(ButtonInteractionEvent event) { }

		@Override
		public void onSelectInteract(SelectMenuInteractionEvent event) { }
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
