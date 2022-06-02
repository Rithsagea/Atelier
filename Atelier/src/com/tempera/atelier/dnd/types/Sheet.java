package com.tempera.atelier.dnd.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.mongojack.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rithsagea.util.event.EventBus;
import com.rithsagea.util.event.EventHandler;
import com.rithsagea.util.event.EventPriority;
import com.rithsagea.util.event.Listener;
import com.rithsagea.util.rand.Dice;
import com.tempera.atelier.dnd.events.LoadHitPointsEvent;
import com.tempera.atelier.dnd.events.LoadProficiencyEvent.LoadEquipmentProficiencyEvent;
import com.tempera.atelier.dnd.events.LoadProficiencyEvent.LoadSavingProficiencyEvent;
import com.tempera.atelier.dnd.events.LoadProficiencyEvent.LoadSkillProficiencyEvent;
import com.tempera.atelier.dnd.events.LoadSheetEvent;
import com.tempera.atelier.dnd.types.character.AbstractClass;
import com.tempera.atelier.dnd.types.character.Attribute;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.EquipmentType;
import com.tempera.atelier.dnd.types.enums.Skill;
import com.tempera.atelier.dnd.types.equipment.Inventory;
import com.tempera.atelier.dnd.types.spread.AbilitySpread;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sheet implements Listener {
	
	@Id
	private final UUID id;
	
	private String name;
	private AbilitySpread baseScores;
	
	private int hitPoints;
	private int maxHitPoints;
	private transient Dice hitDice;
	
	private List<AbstractClass> classes = new ArrayList<>();
	private Inventory inventory = new Inventory();
	
	private transient EventBus eventBus = new EventBus();
	
	private transient Set<Ability> savingProficiencies = new HashSet<>();
	private transient Set<Skill> skillProficiencies = new HashSet<>();
	private transient Set<EquipmentType> equipmentProficiencies = new HashSet<>();
	
	private transient Map<String, Attribute> attributes = new HashMap<>();
	
	@JsonCreator
	public Sheet(@Id UUID id) {
		this.id = id;
		hitDice = new Dice();
	}
	
	public Sheet() {
		this(UUID.randomUUID());
	}
	
	@Override
	public String toString() {
		return String.format("%s [%s]", name, id);
	}
	
	//EVENT HANDLERS
	
	public void reload() {
		eventBus.clearListeners();
		eventBus.registerListener(this);
		
		for(AbstractClass c : classes) {
			eventBus.registerListener(c);
			for(Entry<String, Attribute> entry : c.getAttributeMap().entrySet()) {
				eventBus.registerListener(entry.getValue());
				attributes.put(entry.getKey(), entry.getValue());
			}
		}
		
		eventBus.submitEvent(new LoadSheetEvent(this));
		
		eventBus.submitEvent(new LoadSavingProficiencyEvent(this));
		eventBus.submitEvent(new LoadSkillProficiencyEvent(this));
		eventBus.submitEvent(new LoadEquipmentProficiencyEvent(this));
		eventBus.submitEvent(new LoadHitPointsEvent(this));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onLoadSavingProficiency(LoadSavingProficiencyEvent e) {
		savingProficiencies.clear();
		savingProficiencies.addAll(e.getProficiencies());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onLoadSkillProficiency(LoadSkillProficiencyEvent e) {
		skillProficiencies.clear();
		skillProficiencies.addAll(e.getProficiencies());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onLoadEquipmentProficiency(LoadEquipmentProficiencyEvent e) {
		equipmentProficiencies.clear();
		equipmentProficiencies.addAll(e.getProficiencies());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onLoadHitPoints(LoadHitPointsEvent e) {
		maxHitPoints = e.getMaxHitPoints();
		hitPoints = Math.min(maxHitPoints, hitPoints);
		
		hitDice = e.getHitDice();
	}
	
	//ACCESSORS
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public AbilitySpread getBaseScores() {
		return baseScores;
	}
	
	public int getAbilityScore(Ability ability) {
		return baseScores.getBaseScore(ability);
	}
	
	public int getAbilityModifier(Ability ability) {
		return (baseScores.getBaseScore(ability) - 10) / 2;
	}
	
	public int getSavingModifier(Ability ability) {
		if(hasSavingProficiency(ability))
			return getAbilityModifier(ability) + 2;
		return getAbilityModifier(ability);
	}
	
	public int getSkillModifier(Skill skill) {
		if(hasSkillProficiency(skill))
			return getAbilityModifier(skill.getAbility()) + 2;
		return getAbilityModifier(skill.getAbility());
	}
	
	public boolean hasSavingProficiency(Ability ability) {
		return savingProficiencies.contains(ability);
	}
	
	public boolean hasSkillProficiency(Skill skill) {
		return skillProficiencies.contains(skill);
	}
	
	public boolean hasEquipmentProficiency(EquipmentType equipment) {
		return equipmentProficiencies.contains(equipment);
	}
	
	public int getHitPoints() {
		return hitPoints;
	}
	
	public int getMaxHitPoints() {
		return maxHitPoints;
	}
	
	public Dice getHitDice() {
		return hitDice;
	}
	
	public List<AbstractClass> getClasses() {
		return Collections.unmodifiableList(classes);
	}
	
	public Attribute getAttribute(String id) {
		return attributes.get(id);
	}
	
	public Collection<Attribute> getAttributes() {
		return attributes.values();
	}
	
	public Map<String, Attribute> getAttributeMap() {
		return Collections.unmodifiableMap(attributes);
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	//MUTATORS
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setBaseScores(AbilitySpread spread) {
		baseScores = spread;
	}
	
	public void setHitPoints(int health) {
		this.hitPoints = Math.min(health, maxHitPoints);
	}
	
	@Deprecated
	public void clearClasses() {
		classes.clear();
	}
	
	public void addClass(AbstractClass clazz) {
		classes.add(clazz);
	}
}