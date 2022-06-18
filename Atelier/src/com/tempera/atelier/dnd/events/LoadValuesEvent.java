package com.tempera.atelier.dnd.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tempera.atelier.dnd.types.Sheet;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Skill;

public class LoadValuesEvent<K, V> extends LoadSheetEvent {

	private Map<K, V> values;

	public LoadValuesEvent(Sheet sheet, Map<K, V> values) {
		super(sheet);
		
		this.values = new HashMap<>(values);
	}

	public Set<K> getKeys() {
		return values.keySet();
	}

	public V getValue(K key) {
		return values.get(key);
	}

	public void setValue(K key, V value) {
		values.put(key, value);
	}

	public static class LoadAbilityScoreEvent extends LoadValuesEvent<Ability, Integer> {
		public LoadAbilityScoreEvent(Sheet sheet, Map<Ability, Integer> values) {
			super(sheet, values);
		}
	}

	public static class LoadAbilityModifierEvent extends LoadValuesEvent<Ability, Integer> {
		public LoadAbilityModifierEvent(Sheet sheet, Map<Ability, Integer> values) {
			super(sheet, values);
		}
	}
	
	public static class LoadSavingModifierEvent extends LoadValuesEvent<Ability, Integer> {
		public LoadSavingModifierEvent(Sheet sheet, Map<Ability, Integer> values) {
			super(sheet, values);
		}
	}
	
	public static class LoadSkillModifierEvent extends LoadValuesEvent<Skill, Integer> {
		public LoadSkillModifierEvent(Sheet sheet, Map<Skill, Integer> values) {
			super(sheet, values);
		}
	}
}
