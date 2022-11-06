package com.atelier.dnd.events;

import java.util.Map;

import com.atelier.dnd.Ability;
import com.atelier.dnd.Skill;
import com.atelier.dnd.character.AtelierCharacter;
import com.atelier.dnd.events.LoadEvent.LoadCharacterEvent;

public class LoadValuesEvent<K extends Enum<K>, V> extends LoadCharacterEvent {
	
	private final Map<K, V> values;
	
	public LoadValuesEvent(AtelierCharacter character, Map<K, V> map) {
		super(character);
		values = map;
	}
	
	protected V get(K key) { return values.get(key); }
	protected void set(K key, V value) { values.put(key, value); }
	
	public static class LoadAbilityScoreEvent extends LoadValuesEvent<Ability, Integer> {
		public LoadAbilityScoreEvent(AtelierCharacter character, Map<Ability, Integer> values) {
			super(character, values);
		}
		
		public int getAbilityScore(Ability ability) { return get(ability); }
		public void setAbilityScore(Ability ability, int score) { set(ability, score); }
	}
	
	public static class LoadAbilityModifierEvent extends LoadValuesEvent<Ability, Integer> {
		public LoadAbilityModifierEvent(AtelierCharacter character, Map<Ability, Integer> values) {
			super(character, values);
		}
		
		public int getAbilityModifier(Ability ability) { return get(ability); }
		public void setAbilityModifier(Ability ability, int modifier) { set(ability, modifier); }
	}
	
	public static class LoadSavingModifierEvent extends LoadValuesEvent<Ability, Integer> {
		public LoadSavingModifierEvent(AtelierCharacter character, Map<Ability, Integer> values) {
			super(character, values);
		}
		
		public int getSavingModifier(Ability ability) { return get(ability); }
		public void setSavingModifier(Ability ability, int modifier) { set(ability, modifier); }
	}
	
	public static class LoadSkillModifierEvent extends LoadValuesEvent<Skill, Integer> {
		public LoadSkillModifierEvent(AtelierCharacter character, Map<Skill, Integer> values) {
			super(character, values);
		}
		
		public int getSkillModifier(Skill skill) { return get(skill); }
		public void setSkillModifier(Skill skill, int modifier) { set(skill, modifier); }
	}
}
