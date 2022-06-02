package com.tempera.atelier.dnd.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.tempera.atelier.dnd.types.character.AbstractClass;
import com.tempera.atelier.dnd.types.equipment.Item;
import com.tempera.atelier.dnd.types.spread.AbilitySpread;

public class TypeRegistry {

	private Map<Class<?>, Map<String, Class<?>>> typeMap;

	public TypeRegistry() {
		typeMap = new HashMap<>();

		addSupertype(AbilitySpread.class);
		addSupertype(AbstractClass.class);
		addSupertype(Item.class);
	}

	private void addSupertype(Class<?> clazz) {
		typeMap.put(clazz, new TreeMap<>());
	}

	public void registerType(String id, Class<?> clazz) {
		for (Entry<Class<?>, Map<String, Class<?>>> e : typeMap.entrySet()) {
			if (e.getKey()
				.isAssignableFrom(clazz))
				e.getValue()
					.put(id, clazz);
		}
	}

	public Map<String, Class<?>> getSubtypes(Class<?> superclass) {
		return Collections.unmodifiableMap(typeMap.get(superclass));
	}
}
