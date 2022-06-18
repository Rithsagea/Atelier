package com.tempera.atelier.dnd.events;

import com.rithsagea.util.event.AtomicEvent;
import com.rithsagea.util.event.Event;
import com.tempera.atelier.dnd.types.Sheet;

@AtomicEvent
public class LoadSheetEvent implements Event {
	private Sheet sheet;

	public LoadSheetEvent(Sheet sheet) {
		this.sheet = sheet;
	}

	public Sheet getSheet() {
		return sheet;
	}
}
