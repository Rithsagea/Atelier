package com.atelier.test;

import java.util.UUID;

import com.atelier.Config;
import com.atelier.database.AtelierDB;
import com.atelier.discord.User;
import com.atelier.dnd.types.Sheet;

public class DBTest {
	public static void main(String[] args) {
		Config config = Config.init("config.properties");
		AtelierDB db = AtelierDB.init(config);

		editDB(db);

		db.save();
	}

	public static void editDB(AtelierDB db) {
		long userId = 171378138041942016l;
		UUID sheetId = UUID.fromString("12345678-1234-1234-1234-12345678");
//		User user = new User(userId);
//		Sheet sheet = new Sheet(sheetId);
//		db.addUser(user);
//		db.addSheet(sheet);
		
		User user = db.getUser(userId);
		Sheet sheet = db.getSheet(sheetId);

		user.setName("Rithsagea");
		System.out.println(sheet.getId());
	}
}
