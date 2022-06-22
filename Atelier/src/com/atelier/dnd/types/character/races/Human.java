package com.atelier.dnd.types.character.races;

import com.atelier.dnd.types.IndexedItem;
import com.atelier.dnd.types.character.CharacterRace;
import com.atelier.dnd.types.character.races.traits.AgeTrait;
import com.atelier.dnd.types.character.races.traits.SizeTrait;
import com.atelier.dnd.types.enums.Size;

@IndexedItem("human")
public class Human extends CharacterRace {

	public Human() {
		super("human", "Human");
		
		addTrait("age", new AgeTrait("Humans reach adulthood in their late teens and live less than a century."));
		addTrait("size", new SizeTrait(Size.MEDIUM, "Humans vary widely in height and build, from barely 5 feet to well over 6 feet tall. Regardless of your position in that range, your size is Medium."));
	}

}
