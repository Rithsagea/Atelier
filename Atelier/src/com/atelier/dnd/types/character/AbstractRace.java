package com.atelier.dnd.types.character;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rithsagea.util.event.Listener;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface AbstractRace extends Listener {
	public String getId();

	public String getName();
	
	public Map<String, CharacterAttribute> getTraits();
}
