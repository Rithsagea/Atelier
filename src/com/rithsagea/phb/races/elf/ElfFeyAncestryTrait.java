package com.rithsagea.phb.races.elf;

import com.atelier.database.AtelierType;
import com.atelier.discord.Menu;
import com.atelier.dnd.character.attributes.DescriptionMenu;
import com.atelier.dnd.character.attributes.RacialTrait;

//TODO functionality
@AtelierType("elf-fey-ancestry")
public class ElfFeyAncestryTrait extends RacialTrait {
	@Override
	public Menu getMenu() {
		return new DescriptionMenu(this);
	}
}
