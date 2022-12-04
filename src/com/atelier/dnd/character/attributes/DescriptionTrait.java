package com.atelier.dnd.character.attributes;

import com.atelier.discord.Menu;

public class DescriptionTrait extends RacialTrait {
	@Override
	public Menu getMenu() {
		return new DescriptionMenu(this);
	}
}
