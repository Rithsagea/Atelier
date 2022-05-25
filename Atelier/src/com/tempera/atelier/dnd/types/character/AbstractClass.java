package com.tempera.atelier.dnd.types.character;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="type")
public interface AbstractClass {
	
	public String getId();
	public String getName();
	
	public int getLevel();
	
	public Map<String, Attribute> getAttributeMap();
	
	//TODO multiclassing flag somewhere
}
