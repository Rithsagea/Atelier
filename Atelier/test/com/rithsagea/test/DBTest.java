package com.rithsagea.test;

import com.rithsagea.atelier.Config;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.Ability;
import com.rithsagea.atelier.dnd.Sheet;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.database.AtelierDB;
import com.rithsagea.atelier.dnd.types.spread.PointBuySpread;

public class DBTest {
	public static void main(String[] args) {
		Config config = new Config("config.properties");
		AtelierDB db = new AtelierDB(config);
		
		editDB(db);
		
		db.save();
	}
	
	public static void editDB(AtelierDB db) {
		User user = db.getUser(529463250052448256l);
		user.setLevel(PermissionLevel.ADMINISTRATOR);
		
		Sheet sheet = db.getSheet(user.getSheetId());
		PointBuySpread spread = (PointBuySpread) sheet.getBaseScores();
		for(Ability ability : Ability.values())
			spread.setScore(ability, 8);
	}
}
