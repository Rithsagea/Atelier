package com.atelier.dnd;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.atelier.AtelierObject;

public enum Ability implements AtelierObject {
	STRENGTH,
	DEXTERITY,
	CONSTITUTION,
	INTELLIGENCE,
	WISDOM,
	CHARISMA;
	
	private String name;
	private String label;
	
	private Ability() {
		name = getProperty("name");
		label = getProperty("label");
	}
	
	public String getName() {
		return name;
	}
	
	public String getLabel() {
		return label;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	private static Map<String, Ability> LABEL_MAP;
	static {
		LABEL_MAP = Stream.of(Ability.values())
				.collect(Collectors.toMap(
						a->a.getLabel(),
						Function.identity()));
	}
	public static Ability fromLabel(String label) {
		return LABEL_MAP.get(label);
	}
}
