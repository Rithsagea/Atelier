package com.atelier.dnd.events;

import com.atelier.dnd.types.Sheet;
import com.atelier.dnd.types.character.AbstractClass;
import com.atelier.dnd.types.character.AbstractRace;
import com.rithsagea.util.event.AtomicEvent;
import com.rithsagea.util.event.Event;

public class LoadEvent<T> implements Event {
	private T obj;

	public LoadEvent(T obj) {
		this.obj = obj;
	}

	protected T get() {
		return obj;
	}

	@AtomicEvent
	public static class LoadSheetEvent extends LoadEvent<Sheet> {
		public LoadSheetEvent(Sheet obj) { super(obj); }
		public Sheet getSheet() { return get(); }
	}
	
	@AtomicEvent
	public static class LoadClassEvent extends LoadEvent<AbstractClass> {
		public LoadClassEvent(AbstractClass obj) { super(obj); }
		public AbstractClass getCharacterClass() { return get();}
	}

	@AtomicEvent
	public static class LoadRaceEvent extends LoadEvent<AbstractRace> {
		public LoadRaceEvent(AbstractRace race) { super(race); }
		public AbstractRace getRace() { return get(); }
	}
}
