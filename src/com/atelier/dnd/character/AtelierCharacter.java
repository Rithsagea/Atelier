package com.atelier.dnd.character;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.atelier.database.AtelierDB;
import com.atelier.dnd.Ability;
import com.atelier.dnd.AbilitySpread;
import com.atelier.dnd.Skill;
import com.atelier.dnd.campaign.Campaign;
import com.atelier.dnd.character.attributes.CharacterAttribute;
import com.atelier.dnd.character.attributes.CharacterClass;
import com.atelier.dnd.character.attributes.CharacterRace;
import com.atelier.dnd.events.LoadEvent.LoadCharacterEvent;
import com.atelier.dnd.events.LoadProficiencyEvent.LoadSavingProficiencyEvent;
import com.atelier.dnd.events.LoadProficiencyEvent.LoadSkillProficiencyEvent;
import com.atelier.dnd.events.LoadValuesEvent.LoadAbilityModifierEvent;
import com.atelier.dnd.events.LoadValuesEvent.LoadAbilityScoreEvent;
import com.atelier.dnd.events.LoadValuesEvent.LoadSavingModifierEvent;
import com.atelier.dnd.events.LoadValuesEvent.LoadSkillModifierEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rithsagea.util.event.EventBus;
import com.rithsagea.util.event.EventHandler;
import com.rithsagea.util.event.EventPriority;
import com.rithsagea.util.event.Listener;

public class AtelierCharacter implements Listener {
	
	@JsonProperty("_id")
	private UUID id;
	private String name = "";
	
	private AbilitySpread abilitySpread = new AbilitySpread();
	private CharacterClass characterClass = new NullClass();
	private CharacterRace characterRace = new NullRace();

	private static final int[] EXPERIENCE_TABLE = { 0, 300, 900, 2700, 6500, 14000, 23000, 34000, 48000, 64000, 85000, 100000, 120000, 140000, 165000, 195000, 225000, 265000, 305000, 355000 };
	private int experiencePoints = 0;
	private int level = 0;
	
	private UUID campaign;

	private transient EventBus eventBus = new EventBus();
	
	private transient Set<Ability> savingProficiencies = EnumSet.noneOf(Ability.class);
	private transient Set<Skill> skillProficiencies = EnumSet.noneOf(Skill.class);
	
	private transient Map<Ability, Integer> abilityScores = new EnumMap<>(Ability.class);
	private transient Map<Ability, Integer> abilityModifiers = new EnumMap<>(Ability.class);
	private transient Map<Ability, Integer> savingModifiers = new EnumMap<>(Ability.class);
	private transient Map<Skill, Integer> skillModifiers = new EnumMap<>(Skill.class);
	
	public AtelierCharacter() {
		this(UUID.randomUUID());
	}
	
	public AtelierCharacter(UUID id) {
		this.id = id;
	}
	
	public void reload() {
		eventBus.clearListeners();
		eventBus.registerListener(this);
		eventBus.registerListener(characterClass);
		eventBus.registerListener(characterRace);
		getAttributes().forEach(eventBus::registerListener);

		for(int l = 0; l < 20 && experiencePoints >= EXPERIENCE_TABLE[l]; l++, level = l);
		
		eventBus.submitEvent(new LoadCharacterEvent(this));
		eventBus.submitEvent(new LoadSavingProficiencyEvent(this));
		eventBus.submitEvent(new LoadSkillProficiencyEvent(this));
		
		eventBus.submitEvent(new LoadAbilityScoreEvent(this,
			Stream.of(Ability.values()).collect(Collectors.toMap(
				Function.identity(), this::getBaseScore))));
		eventBus.submitEvent(new LoadAbilityModifierEvent(this, 
			Stream.of(Ability.values()).collect(Collectors.toMap( 
				Function.identity(), a -> (getAbilityScore(a) - 10) / 2))));
		eventBus.submitEvent(new LoadSavingModifierEvent(this,
			Stream.of(Ability.values()).collect(Collectors.toMap(
				Function.identity(), a -> (getAbilityModifier(a) + (hasSavingProficiency(a) ? getProficiencyBonus() : 0))))));
		eventBus.submitEvent(new LoadSkillModifierEvent(this,
			Stream.of(Skill.values()).collect(Collectors.toMap(
				Function.identity(), s -> (getAbilityModifier(s.getAbility()) + (hasSkillProficiency(s) ? getProficiencyBonus() : 0))))));
	}
	
	// Handlers
	
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
	private void onLoadAbilityScore(LoadAbilityScoreEvent e) {
		Stream.of(Ability.values()).forEach(a -> abilityScores.put(a, e.getAbilityScore(a)));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onLoadAbilityModifier(LoadAbilityModifierEvent e) {
		Stream.of(Ability.values()).forEach(a -> abilityModifiers.put(a, e.getAbilityModifier(a)));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onLoadSavingModifier(LoadSavingModifierEvent e) {
		Stream.of(Ability.values()).forEach(a -> savingModifiers.put(a, e.getSavingModifier(a)));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onLoadSkillModifier(LoadSkillModifierEvent e) {
		Stream.of(Skill.values()).forEach(s -> skillModifiers.put(s, e.getSkillModifier(s)));
	}

	// ACCESSORS
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasSavingProficiency(Ability ability) {
		return savingProficiencies.contains(ability);
	}

	public boolean hasSkillProficiency(Skill skill) {
		return skillProficiencies.contains(skill);
	}
	
	public AbilitySpread getAbilitySpread() {
		return abilitySpread;
	}
	
	public int getBaseScore(Ability ability) {
		return abilitySpread.getScore(ability);
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
		return skillModifiers.get(skill);
	}

	public CharacterClass getCharacterClass() {
		return characterClass;
	}

	public CharacterRace getCharacterRace() {
		return characterRace;
	}

	public Stream<CharacterAttribute> getAttributes() {
		return Stream.of(
			characterClass.getFeatures().values(),
			characterRace.getTraits().values()).flatMap(Collection::stream);
	}

	public int getExperiencePoints() {
		return experiencePoints;
	}

	public int getLevel() {
		return level;
	}

	public Campaign getCampaign() {
		return AtelierDB.getInstance().getCampaign(campaign);
	}
	
	public EventBus getEventBus() {
		return eventBus;
	}
	
	// MUTATORS
	
	public void setName(String name) {
		this.name = name;
	}

	public void setCharacterClass(CharacterClass characterClass) {
		this.characterClass = characterClass;
	}

	public void setCharacterRace(CharacterRace characterRace) {
		this.characterRace = characterRace;
	}

	public void addExperience(int experiencePoints) {
		this.experiencePoints += experiencePoints;
		while(level < 20 && this.experiencePoints >= EXPERIENCE_TABLE[level]) {
			level++;
			characterClass.levelUp();
		}
	}

	public int getProficiencyBonus() {
		return (level - 1) / 4 + 2;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign == null ? null : campaign.getId();
	}
	
	@Override
	public String toString() {
		return String.format("%s [%s]", name, id);
	}
}
