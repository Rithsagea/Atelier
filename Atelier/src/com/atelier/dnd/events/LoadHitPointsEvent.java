package com.atelier.dnd.events;

import com.atelier.dnd.events.LoadEvent.LoadSheetEvent;
import com.atelier.dnd.types.Sheet;
import com.rithsagea.util.rand.Dice;

public class LoadHitPointsEvent extends LoadSheetEvent {
	private Dice hitDice;
	private int maxHitPoints;

	public LoadHitPointsEvent(Sheet sheet) {
		super(sheet);
		hitDice = new Dice();
	}

	public Dice getHitDice() {
		return hitDice.clone();
	}

	public int getMaxHitPoints() {
		return maxHitPoints;
	}

	public void addHitDice(Dice dice) {
		hitDice.addDice(dice);
	}

	public void setMaxHitPoints(int hitPoints) {
		maxHitPoints = hitPoints;
	}

	public void addMaxHitPoints(int hitPoints) {
		maxHitPoints += hitPoints;
	}
}
