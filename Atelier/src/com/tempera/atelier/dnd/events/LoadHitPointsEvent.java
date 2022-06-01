package com.tempera.atelier.dnd.events;

import com.rithsagea.util.rand.Dice;
import com.tempera.atelier.dnd.types.Sheet;

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
