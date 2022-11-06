package com.atelier.dnd.events;

import com.atelier.dnd.AtelierCharacter;
import com.atelier.dnd.character.CharacterClass;
import com.atelier.dnd.character.CharacterRace;
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

	public static class LoadCharacterClassEvent extends LoadEvent<CharacterClass> {
		public LoadCharacterClassEvent(CharacterClass characterClass) { super(characterClass); }
		public CharacterClass getCharacterClass() { return get(); }
	}

	public static class LoadCharacterRaceEvent extends LoadEvent<CharacterRace> {
		public LoadCharacterRaceEvent(CharacterRace characterRace) { super(characterRace); }
		public CharacterRace getCharacterRace() { return get(); }
	}
}
