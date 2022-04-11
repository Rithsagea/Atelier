package com.rithsagea.test;

import com.rithsagea.atelier.Config;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.User;
import com.rithsagea.atelier.dnd.database.AtelierDB;

public class DBTest {
	public static void main(String[] args) {
		Config config = new Config("config.properties");
		AtelierDB db = new AtelierDB(config);
		
		editDB(db);
		
		db.save();
	}
	
	public static void editDB(AtelierDB db) {
		User user = db.getUser(0l); // Put your Discord ID here
		user.setLevel(PermissionLevel.ADMINISTRATOR);
	}
}
