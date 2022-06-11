package com.tempera.atelier.dnd.types.character;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rithsagea.util.event.Listener;
import com.tempera.atelier.aaagarbage.Menu;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface CharacterAttribute extends Listener {

	public String getName();

	public Menu getMenu();

}
