package com.tempera.test;

import com.tempera.atelier.Config;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.database.AtelierDB;
import com.tempera.atelier.dnd.types.character.classes.Rogue;
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
		User user = db.getUser(529463250052448256l);
		user.setLevel(PermissionLevel.ADMINISTRATOR);
		
		Sheet sheet = db.getSheet(user.getSheetId());
		
		sheet.addClass(new Rogue());
		sheet.reload();
		sheet.setHitPoints(10000);
		
		System.out.println("User: " + user);
		System.out.println("Name: " + sheet.getName());
		for(Ability a : Ability.values()) {
			System.out.printf("%s%s: %d [%s]\n",
					sheet.hasSavingProficiency(a) ? "*" : "",
					WordUtil.capitalize(a.name()),
					sheet.getAbilityScore(a), 
					WordUtil.formatModifier(sheet.getAbilityModifier(a)));
		}
	}
}
