package com.atelier.dnd.events;

import com.atelier.dnd.AtelierCharacter;
import com.rithsagea.util.event.AtomicEvent;
import com.rithsagea.util.event.Event;

@AtomicEvent
public class LoadEvent<T> implements Event {
	private T obj;
	
	public LoadEvent(T obj) {
		this.obj = obj;
	}
	
	protected T get() {
		return obj;
	}
	
	@AtomicEvent
	public static class LoadCharacterEvent extends LoadEvent<AtelierCharacter> {
		public LoadCharacterEvent(AtelierCharacter character) { super(character); }
		public AtelierCharacter getCharacter() { return get(); }
	}
}
