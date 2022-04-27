package com.tempera.atelier.dnd.types.character;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="type")
public interface AbstractClass {
	
	public String getName();
	
	public int getLevel();
	
	public Set<Attribute> getAttributes();
	
	//TODO multiclassing flag somewhere
}
