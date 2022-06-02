package com.tempera.atelier.dnd.events;

import com.rithsagea.util.event.Event;
import com.tempera.atelier.dnd.types.character.AbstractClass;

public class LoadClassEvent implements Event {
	private AbstractClass c;

	public LoadClassEvent(AbstractClass c) {
		this.c = c;
	}

	public AbstractClass getCharacterClass() {
		return c;
	}
}
