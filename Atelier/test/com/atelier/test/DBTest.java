package com.atelier.test;

import com.atelier.Config;
import com.atelier.discord.User;
import com.atelier.dnd.types.AtelierDB;
import com.atelier.dnd.types.Sheet;
import com.atelier.dnd.types.character.classes.Rogue;
import com.atelier.dnd.types.enums.Ability;
import com.atelier.util.WordUtil;

public class DBTest {
	public static void main(String[] args) {
		Config config = Config.init("config.properties");
		AtelierDB db = AtelierDB.init(config);

		editDB(db);

		db.save();
	}

	public static void editDB(AtelierDB db) {
		User user = db.getUser(171378138041942016l);

		Sheet sheet = user.getSheet();

		sheet.clearClasses();
		sheet.addClass(new Rogue());
		sheet.reload();
		sheet.setHitPoints(10000);

		System.out.println("User: " + user);
		System.out.println("Name: " + sheet.getName());
		for (Ability a : Ability.values()) {
			System.out.printf("%s%s: %d [%s]\n",
				sheet.hasSavingProficiency(a) ? "*" : "",
				WordUtil.capitalize(a.name()), sheet.getAbilityScore(a),
				WordUtil.formatModifier(sheet.getAbilityModifier(a)));
		}
	}
}
