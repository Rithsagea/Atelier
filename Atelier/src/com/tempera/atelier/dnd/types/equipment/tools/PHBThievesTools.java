package com.tempera.atelier.dnd.types.equipment.tools;

import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.enums.Currency;
import com.tempera.atelier.dnd.types.enums.Currency.Price;
import com.tempera.atelier.dnd.types.equipment.Tools;

@IndexedItem("phb-thieves-tools")
public class PHBThievesTools extends Tools {

	public PHBThievesTools() {
		super("Thieves Tools",
			"This set of tools includes a small file, a set of lock picks, a small mirror mounted on a metal handle, a set of narrow-bladed scissors, and a pair of pliers. Proficiency with these tools lets you add your proficiency bonus to any ability checks you make to disarm traps or open locks.\n\n"
				+ "Perhaps the most common tools used by adventurers, thieves' tools are designed for picking locks and foiling traps. Proficiency with the tools also grants you a general knowledge of traps and locks.",
			"PHB, page 154. Available in the SRD and the Basic Rules.",
			new Price(25, Currency.GOLD), 1);
	}

}
