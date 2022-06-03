package com.tempera.test;

import com.tempera.atelier.dnd.types.Sheet;
import com.tempera.atelier.dnd.types.character.classes.Rogue;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.spread.PointBuySpread;

public class SheetTest {
	public static void main(String[] args) {
		Sheet sheet = new Sheet();
		sheet.setName("Lita");
		
		PointBuySpread spread = new PointBuySpread();
		spread.setScore(Ability.WISDOM, 15);
		spread.setScore(Ability.CHARISMA, 15);
		spread.setScore(Ability.DEXTERITY, 15);
		sheet.setBaseScores(spread);
		
		sheet.clearClasses();
		sheet.addClass(new Rogue());
		sheet.setHitPoints(10000);
		
		sheet.reload();
	}
}
