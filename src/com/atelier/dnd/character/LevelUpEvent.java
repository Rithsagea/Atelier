package com.atelier.dnd.character;

import com.rithsagea.util.event.Event;

public class LevelUpEvent implements Event {
	
	private int level;
	
	public LevelUpEvent(int level) {
		this.level = level;
	}

	/**
	 * 
	 * @return the level which this class will be
	 */
	public int getLevel() {
		return level;
	}
}
