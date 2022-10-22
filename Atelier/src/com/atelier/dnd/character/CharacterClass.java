package com.atelier.dnd.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.atelier.AtelierObject;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.rithsagea.util.event.EventBus;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "_cls", defaultImpl = Void.class)
public abstract class CharacterClass implements AtelierObject {
	
	public static class TestClass extends CharacterClass {
		public TestClass() {
			registerAttribute(1, "1-test", new TestAttribute());
		}
	}

	public static class TestAttribute extends CharacterAttribute {
		public String test = "foo";
	}

	private transient EventBus eventBus = new EventBus();
	private int level = 0;
	
	private Map<String, CharacterAttribute> attributes = new HashMap<>();
	
	private transient List<Map<String, CharacterAttribute>> levelAttributeMap;

	public CharacterClass() {
		levelAttributeMap = new ArrayList<>();
		IntStream.range(0, 20).forEach(x -> levelAttributeMap.add(new HashMap<>()));
	}

	protected void registerAttribute(int level, String key, CharacterAttribute attribute) {
		levelAttributeMap.get(level - 1).put(key, attribute);
	}

	public String getName() { 
		return getProperty("name");
	}
	
	public int getLevel() {
		return level;
	}

	//TODO remove debug method
	@Deprecated
	public void levelUp() {
		level++;
		attributes.putAll(levelAttributeMap.get(level - 1));
	}
	
	public EventBus getEventBus() {
		return eventBus;
	}
	
	public Map<String, CharacterAttribute> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}
	
	@Override
	public String toString() {
		return getName() + " " + level;
	}
}
