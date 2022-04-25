package com.tempera.atelier.dnd.types.character;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tempera.atelier.dnd.types.enums.Proficiency;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="type")
public interface AbstractClass {
	
	
	public String getName();
	
	public int getLevel();
	public boolean hasProficiency(Proficiency proficiency);
	
	//TODO multiclassing flag somewhere
}
