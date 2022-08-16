package com.atelier.database;

import com.atelier.discord.AtelierUser;
import com.atelier.dnd.AtelierCharacter;

public class Types {
	public static void registerTypes(TypeRegistry r) {
		r.registerType(AtelierUser.class);
		r.registerType(AtelierCharacter.class);
	}
}
