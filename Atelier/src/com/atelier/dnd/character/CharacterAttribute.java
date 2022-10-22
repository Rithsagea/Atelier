package com.atelier.dnd.character;

import com.atelier.AtelierObject;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.rithsagea.util.event.Listener;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "_cls", defaultImpl = Void.class)
public class CharacterAttribute implements Listener, AtelierObject {
	public String getName() {
		return getProperty("name");
	}
}
