package com.rithsagea.atelier.dnd.database;

import java.util.UUID;

import com.rithsagea.atelier.Config;
import com.rithsagea.atelier.dnd.database.types.Ability;

public class DBTest {
	public static void main(String[] args) {
		Config config = new Config("config.properties");
		AtelierDB db = new AtelierDB(config);
		
		User user = db.findUser(171378138041942016l);
		Sheet sheet = db.findSheet(UUID.fromString("1ced756c-dbc6-4be5-af0f-d6e66beffb0e"));
		
		for(Sheet s : db.listSheets()) {
			System.out.println(s.getId());
		}
		
		user.setSheetId(sheet.getId());
		
		System.out.println(user.getName() + ": " + user.getId());
		System.out.println("Name: " + sheet.getName());
		
		for(Ability ability : Ability.values() ) {
			System.out.println("\t" + ability + ": " + sheet.getAbilityScore(ability));
		}
		
		db.updateUser(user);
		db.updateSheet(sheet);
	}
}
