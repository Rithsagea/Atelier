package com.atelier.dnd.types.equipment.armor;

import com.atelier.dnd.types.enums.Currency;
import com.atelier.dnd.types.enums.Currency.Price;
import com.atelier.dnd.types.equipment.Armor;

public class PHBChainMail extends Armor{

	public PHBChainMail() {
		super("Chain Mail", "Made of interlocking metal rings, chain mail includes a layer of quilted fabric worn underneath the mail to prevent chafing and to cushion the impact of blows. The suit includes gauntlets.\r\n" + 
			"\n" + "The wearer has disadvantage on Dexterity (Stealth) checks.\n" + 
			"\n" + "If the wearer has a Strength score lower than 13, their speed is reduced by 10 feet.",
			"PHB, page 145. Available in the SRD and the Basic Rules.", 
			new Price(75, Currency.GOLD), 55);
		// TODO Auto-generated constructor stub
	}

}
