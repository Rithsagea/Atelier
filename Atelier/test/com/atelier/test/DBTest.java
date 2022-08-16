package com.atelier.test;

import java.util.UUID;

import com.atelier.Config;
import com.atelier.database.AtelierDB;
import com.atelier.discord.AtelierUser;
import com.atelier.dnd.AtelierCharacter;

public class DBTest {
	public static void main(String[] args) {
		Config config = Config.init("config.properties");
		AtelierDB db = AtelierDB.init(config);

		editDB(db);

		db.save();
	}

	public static void editDB(AtelierDB db) {
		long userId = 171378138041942016l;
		UUID charId = UUID.fromString("3ba3717b-b2be-4220-bf86-949691e37afe");
//		User user = new User(userId);
//		Sheet sheet = new Sheet(sheetId);
//		db.addUser(user);
//		db.addSheet(sheet);
		
		AtelierUser user = db.getUser(userId);
		
		AtelierCharacter character = db.getCharacter(charId);
//		character.setName("Lita");
		System.out.println(character.getName());
	}
}
