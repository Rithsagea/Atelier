package com.atelier.dnd.character;

import com.atelier.database.AtelierType;
import com.atelier.dnd.character.attributes.CharacterRace;

@AtelierType("null")
public class NullRace extends CharacterRace {
	@Override
	protected void init() {}
}