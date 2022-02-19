package com.rithsagea.atelier.dnd.database;

import java.util.UUID;

import com.rithsagea.atelier.Config;
import com.rithsagea.atelier.dnd.Ability;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.types.spread.PointBuySpread;

public class DBTest {
	
	public static void initializeLita(Sheet sheet) {
		PointBuySpread spread = new PointBuySpread();
		spread.setScore(Ability.STRENGTH, 10);
		spread.setScore(Ability.DEXTERITY, 13);
		spread.setScore(Ability.CONSTITUTION, 10);
		spread.setScore(Ability.INTELLIGENCE, 13);
		spread.setScore(Ability.WISDOM, 12);
		spread.setScore(Ability.CHARISMA, 15);
		
		sheet.setName("Lita");
		sheet.setBaseScores(spread);
	}
	
	public static void printSheet(Sheet sheet) {
		System.out.println("Name: " + sheet.getName());
		
		for(Ability ability : Ability.values() ) {
			System.out.println("\t" + ability + ": " + sheet.getAbilityScore(ability));
		}
	}
	
	public static void main(String[] args) {
		Config config = new Config("config.properties");
		AtelierDB db = new AtelierDB(config);
		
		User user = db.findUser(171378138041942016l);
		Sheet sheet = db.findSheet(UUID.fromString("1ced756c-dbc6-4be5-af0f-d6e66beffb0e"));
		
		for(Sheet s : db.listSheets()) {
			System.out.println(s.getId());
		}
		
		System.out.println(user.getName() + ": " + user.getId());
		
		user.setSheetId(sheet.getId());
		
		initializeLita(sheet);
		printSheet(sheet);
		
		db.updateUser(user);
		db.updateSheet(sheet);
	}
}
