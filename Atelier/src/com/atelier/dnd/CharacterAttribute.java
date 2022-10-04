package com.atelier.dnd;

import com.atelier.AtelierObject;
import com.rithsagea.util.event.Listener;

public class CharacterAttribute implements Listener, AtelierObject {
	public String getName() {
		return getProperty("name");
	}
}
