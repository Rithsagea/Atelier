package com.tempera.test;

import com.tempera.atelier.Config;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.database.AtelierDB;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.util.WordUtil;

public class DBTest {
	public static void main(String[] args) {
		Config config = new Config("config.properties");
		AtelierDB db = new AtelierDB(config);
		
		editDB(db);
		
		db.save();
	}
	
	public static void editDB(AtelierDB db) {
		User user = db.getUser(171378138041942016l);
		user.setLevel(PermissionLevel.ADMINISTRATOR);
		
		Sheet sheet = db.getSheet(user.getSheetId());
		
		System.out.println("User: " + user);
		System.out.println("Name: " + sheet.getName());
		for(Ability a : Ability.values()) {
			System.out.printf("%s: %d [%s]\n", WordUtil.capitalize(a.name()),
					sheet.getAbilityScore(a), WordUtil.formatModifier(sheet.getAbilityModifier(a)));
		}
	}
}
