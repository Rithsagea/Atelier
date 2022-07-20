package com.atelier.dnd.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.atelier.database.Id;
import com.atelier.dnd.events.LoadHitPointsEvent;
import com.atelier.dnd.events.LoadProficiencyEvent.LoadEquipmentProficiencyEvent;
import com.atelier.dnd.events.LoadProficiencyEvent.LoadSavingProficiencyEvent;
import com.atelier.dnd.events.LoadProficiencyEvent.LoadSkillProficiencyEvent;
import com.atelier.dnd.events.LoadValuesEvent.LoadAbilityModifierEvent;
import com.atelier.dnd.events.LoadValuesEvent.LoadAbilityScoreEvent;
import com.atelier.dnd.events.LoadValuesEvent.LoadSavingModifierEvent;
import com.atelier.dnd.events.LoadValuesEvent.LoadSkillModifierEvent;
import com.atelier.dnd.types.character.AbstractClass;
import com.atelier.dnd.types.character.AbstractRace;
import com.atelier.dnd.types.character.CharacterAttribute;
import com.atelier.dnd.types.enums.Ability;
import com.atelier.dnd.types.enums.EquipmentType;
import com.atelier.dnd.types.enums.Skill;
import com.atelier.dnd.types.equipment.Inventory;
import com.atelier.dnd.types.spread.AbilitySpread;
import com.rithsagea.util.event.EventBus;
import com.rithsagea.util.event.EventHandler;
import com.rithsagea.util.event.EventPriority;
import com.rithsagea.util.event.Listener;
import com.rithsagea.util.rand.Dice;

public class Sheet implements Listener {

	@Id
	private final UUID id;

	private String name;
	private AbilitySpread baseScores;// = new PointBuySpread();

	private int hitPoints;
	private int maxHitPoints;
	private transient Dice hitDice;// = new Dice();

	private AbstractRace race ;// = new Human();
	private List<AbstractClass> classes = new ArrayList<>();
	private Inventory inventory;// = new Inventory();

	private transient EventBus eventBus = new EventBus();

	private transient Map<Ability, Integer> abilityScores = new HashMap<>();
	private transient Map<Ability, Integer> abilityModifiers = new HashMap<>();
	private transient Map<Ability, Integer> savingModifiers = new HashMap<>();
	private transient Map<Skill, Integer> skillModifiers = new HashMap<>();
	
	private transient int proficiencyBonus = 2; // 2 + (level - 1) / 4
	
	private transient Set<Ability> savingProficiencies = new HashSet<>();
	private transient Set<Skill> skillProficiencies = new HashSet<>();
	private transient Set<EquipmentType> equipmentProficiencies = new HashSet<>();

	private transient Map<String, CharacterAttribute> attributes = new HashMap<>();

	public Sheet(UUID id) {
		this.id = id;
	}

	public Sheet() {
		this(UUID.randomUUID());
	}

	@Override
	public String toString() {
		return String.format("%s [%s]", name, id);
	}

	// EVENT HANDLERS

	public void reload() {
//		eventBus.clearListeners();
//		eventBus.registerListener(this);
//
//		eventBus.registerListener(race);
//		for(Entry<String, CharacterAttribute> entry : race.getTraits().entrySet()) {
//			eventBus.registerListener(entry.getValue());
//			attributes.put(entry.getKey(), entry.getValue());
//		}
//		
//		for (AbstractClass c : classes) {
//			eventBus.registerListener(c);
//			for (Entry<String, CharacterAttribute> entry : c.getFeatures().entrySet()) {
//				eventBus.registerListener(entry.getValue());
//				attributes.put(entry.getKey(), entry.getValue());
//			}
//		}
//
//		eventBus.submitEvent(new LoadSheetEvent(this));
//
//		eventBus.submitEvent(new LoadSavingProficiencyEvent(this));
//		eventBus.submitEvent(new LoadSkillProficiencyEvent(this));
//		eventBus.submitEvent(new LoadEquipmentProficiencyEvent(this));
//
//		eventBus.submitEvent(new LoadAbilityScoreEvent(this, 
//				Stream.of(Ability.values()).collect(Collectors.toMap(
//						Function.identity(), this::getBaseScore))));
//		eventBus.submitEvent(new LoadAbilityModifierEvent(this, 
//				Stream.of(Ability.values()).collect(Collectors.toMap( 
//						Function.identity(), a -> (getAbilityScore(a) - 10) / 2))));
//		eventBus.submitEvent(new LoadSavingModifierEvent(this,
//				Stream.of(Ability.values()).collect(Collectors.toMap(
//						Function.identity(), a -> (getAbilityModifier(a) + (hasSavingProficiency(a) ? proficiencyBonus : 0))))));
//		eventBus.submitEvent(new LoadSkillModifierEvent(this,
//				Stream.of(Skill.values()).collect(Collectors.toMap(
//						Function.identity(), s -> (getSkillModifier(s) + (hasSkillProficiency(s) ? proficiencyBonus : 0))))));
//		
//		eventBus.submitEvent(new LoadHitPointsEvent(this));
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
	private void onLoadAbilityScore(LoadAbilityScoreEvent e) {
		e.getKeys().forEach(k -> abilityScores.put(k, e.getValue(k)));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onLoadAbilityModifier(LoadAbilityModifierEvent e) {
		e.getKeys().forEach(k -> abilityModifiers.put(k, e.getValue(k)));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onLoadSavingModifier(LoadSavingModifierEvent e) {
		e.getKeys().forEach(k -> savingModifiers.put(k, e.getValue(k)));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onLoadSkillModifier(LoadSkillModifierEvent e) {
		e.getKeys().forEach(k -> skillModifiers.put(k, e.getValue(k)));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onLoadHitPoints(LoadHitPointsEvent e) {
		maxHitPoints = e.getMaxHitPoints();
		hitPoints = Math.min(maxHitPoints, hitPoints);

		hitDice = e.getHitDice();
	}

	// ACCESSORS

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public AbilitySpread getBaseScores() {
		return baseScores;
	}
	
	public int getBaseScore(Ability ability) {
		return baseScores.getBaseScore(ability);
	}
	
	public int getAbilityScore(Ability ability) {
		return abilityScores.get(ability);
	}

	public int getAbilityModifier(Ability ability) {
		return abilityModifiers.get(ability);
	}
	
	public int getSavingModifier(Ability ability) {
		return savingModifiers.get(ability);
	}

	public int getSkillModifier(Skill skill) {
		if (hasSkillProficiency(skill))
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

	public AbstractRace getRace() {
		return race;
	}
	
	public List<AbstractClass> getClasses() {
		return Collections.unmodifiableList(classes);
	}

	public CharacterAttribute getAttribute(String id) {
		return attributes.get(id);
	}

	public Collection<CharacterAttribute> getAttributes() {
		return attributes.values();
	}

	public Map<String, CharacterAttribute> getAttributeMap() {
		return Collections.unmodifiableMap(attributes);
	}

	public Inventory getInventory() {
		return inventory;
	}

	// MUTATORS

	public void setName(String name) {
		this.name = name;
	}

	public void setBaseScores(AbilitySpread spread) {
		baseScores = spread;
	}

	public void setHitPoints(int health) {
		this.hitPoints = Math.min(health, maxHitPoints);
	}
	
	public void setRace(AbstractRace race) {
		this.race = race;
	}
	
	@Deprecated
	public void clearClasses() {
		classes.clear();
	}

	public void addClass(AbstractClass clazz) {
		classes.add(clazz);
	}
}
