package com.rithsagea.phb.races.elf;

import com.atelier.discord.Menu;
import com.atelier.dnd.character.DescriptionMenu;
import com.atelier.dnd.character.RacialTrait;

//TODO functionality
public class ElfDarkvisionTrait extends RacialTrait {
	@Override
	public Menu getMenu() {
		return new DescriptionMenu(this); 
	}
}
