package com.tempera.atelier.dnd.types.character;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rithsagea.util.event.Listener;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="type")
public interface AbstractClass extends Listener {
	
	public String getId();
	public String getName();
	
	public int getLevel();
	
	public Map<String, Attribute> getAttributeMap();
	
	//TODO multiclassing flag somewhere
}
