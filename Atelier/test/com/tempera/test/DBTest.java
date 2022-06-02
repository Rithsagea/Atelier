package com.tempera.test;

import com.tempera.atelier.Config;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.database.AtelierDB;
import com.tempera.atelier.dnd.types.Sheet;
import com.tempera.atelier.dnd.types.character.classes.Rogue;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.equipment.Inventory;
import com.tempera.atelier.dnd.types.equipment.armor.PHBLeatherArmor;
import com.tempera.atelier.dnd.types.equipment.weapons.PHBDagger;
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
		
		sheet.clearClasses();
		sheet.addClass(new Rogue());
		sheet.reload();
		sheet.setHitPoints(10000);
		
		Inventory inventory = sheet.getInventory();
		
		inventory.addItem(new PHBLeatherArmor());
		inventory.addItem(new PHBDagger());
		inventory.addItem(new PHBLeatherArmor());
		inventory.addItem(new PHBDagger());
		inventory.addItem(new PHBLeatherArmor());
		inventory.addItem(new PHBDagger());
		
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
