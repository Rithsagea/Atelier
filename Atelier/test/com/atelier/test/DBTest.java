package com.atelier.test;

import java.util.UUID;

import com.atelier.Config;
import com.atelier.database.AtelierDB;
import com.atelier.discord.AtelierUser;
import com.atelier.dnd.character.AtelierCharacter;
import com.rithsagea.phb.classes.rogue.Rogue;
import com.rithsagea.phb.races.elf.Elf;

public class DBTest {
	public static void main(String[] args) {
		Config config = Config.init("config.properties");
		AtelierDB db = AtelierDB.init(config);
		db.connect();

		editDB(db);

		db.save();
	}

	public static void editDB(AtelierDB db) {
		long userId = 171378138041942016l;
		UUID charId = UUID.fromString("3ba3717b-b2be-4220-bf86-949691e37afe");
		AtelierUser user;
		AtelierCharacter character;
		
//		user = new AtelierUser(userId);
//		user.setName("Rithsagea");
//		db.addUser(user);
		
		user = db.getUser(userId);
		
//		character = new AtelierCharacter(charId);
//		db.addCharacter(character);
		
		character = db.getCharacter(charId);
		character.setCharacterClass(new Rogue());
		character.getCharacterClass().levelUp();
		character.setCharacterRace(new Elf());

		user.addCharacter(character);
		
		System.out.println("Username: " + user.getName());
		System.out.println("User ID: " + user.getId());
		System.out.println();
		System.out.println("Character Name: " + character.getName());
		System.out.println("Character Id: " + character.getId());
		System.out.println("Character Class: " + character.getCharacterClass());
		System.out.println("Character Race: " + character.getCharacterRace());
		System.out.println();
		System.out.println("Character Attributes: ");
		character.getAttributes().forEach(System.out::println);
	}
}
