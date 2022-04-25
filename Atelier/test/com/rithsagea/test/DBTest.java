package com.rithsagea.test;

import com.tempera.atelier.Config;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.database.AtelierDB;
import com.tempera.atelier.dnd.types.character.classes.Rogue;

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
	}
}
