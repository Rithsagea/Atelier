package com.atelier.test;

import com.atelier.Config;
import com.atelier.database.AtelierDB;
import com.atelier.discord.User;

public class DBTest {
	public static void main(String[] args) {
		Config config = Config.init("config.properties");
		AtelierDB db = AtelierDB.init(config);

		editDB(db);

		db.save();
	}

	public static void editDB(AtelierDB db) {
		long id = 171378138041942016l;
		User user = new User(id);
		db.addUser(user);
		
//		User user = db.getUser(id);
		user.setName("Rithsagea");
	}
}
