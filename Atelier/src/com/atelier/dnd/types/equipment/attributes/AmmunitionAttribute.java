package com.atelier.dnd.types.equipment.attributes;

import com.atelier.database.Subtype;

@Subtype("ammunition")
public class AmmunitionAttribute implements ItemAttribute {

	@Override
	public String getName() {
		return "Ammunition";
	}

	@Override
	public String getDescription() {
		return "You can use a weapon that has the ammunition property to make a ranged attack only if you have ammunition to fire from the weapon. Each time you attack with the weapon, you expend one piece of ammunition. Drawing the ammunition from a quiver, case, or other container is part of the attack. Loading a one-handed weapon requires a free hand. At the end of the battle, you can recover half your expended ammunition by taking a minute to search the battlefield.\n\n"
			+ "If you use a weapon that has the ammunition property to make a melee attack, you treat the weapon as an improvised weapon. A sling must be loaded to deal any damage when used in this way.";
	}
	
}
