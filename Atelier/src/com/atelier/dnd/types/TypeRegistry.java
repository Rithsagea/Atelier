package com.atelier.dnd.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.atelier.dnd.types.character.AbstractClass;
import com.atelier.dnd.types.character.CharacterAttribute;
import com.atelier.dnd.types.equipment.Item;
import com.atelier.dnd.types.equipment.attributes.ItemAttribute;
import com.atelier.dnd.types.spread.AbilitySpread;

import java.util.TreeMap;

public class TypeRegistry {

	private Map<Class<?>, Map<String, Class<?>>> typeMap;

	public TypeRegistry() {
		typeMap = new HashMap<>();

		addSupertype(AbilitySpread.class);
		addSupertype(AbstractClass.class);
		addSupertype(CharacterAttribute.class);
		
		addSupertype(Item.class);
		addSupertype(ItemAttribute.class);
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
