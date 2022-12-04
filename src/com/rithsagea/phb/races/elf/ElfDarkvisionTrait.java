package com.rithsagea.phb.races.elf;

import com.atelier.database.AtelierType;
import com.atelier.discord.Menu;
import com.atelier.dnd.character.attributes.DescriptionMenu;
import com.atelier.dnd.character.attributes.RacialTrait;

//TODO functionality
@AtelierType("elf-darkvision")
public class ElfDarkvisionTrait extends RacialTrait {
	@Override
	public Menu getMenu() {
		return new DescriptionMenu(this); 
	}
}
