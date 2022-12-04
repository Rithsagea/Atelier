package com.rithsagea.phb.races.elf;

import com.atelier.database.AtelierType;
import com.atelier.discord.Menu;
import com.atelier.dnd.character.attributes.DescriptionMenu;
import com.atelier.dnd.character.attributes.RacialTrait;

//TODO add functionality
@AtelierType("elf-trance")
public class ElfTranceTrait extends RacialTrait {
	@Override
	public Menu getMenu() {
		return new DescriptionMenu(this);
	}
}
