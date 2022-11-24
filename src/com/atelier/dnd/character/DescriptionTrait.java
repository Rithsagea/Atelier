package com.atelier.dnd.character;

import com.atelier.discord.Menu;

public class DescriptionTrait extends RacialTrait {
	@Override
	public Menu getMenu() {
		return new DescriptionMenu(this);
	}
}
