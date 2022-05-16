package com.tempera.atelier.dnd.commands;

import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.util.WordUtil;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

public class InfoMessageBuilder extends MessageBuilder{
	
	private Sheet sheet;
	
	public InfoMessageBuilder(Sheet sheet)
	{
		this.sheet = sheet;
		String m = "";
		m += "Name: " + sheet.getName() + "\n";
		m += "Class: " + sheet.getClasses().get(0);
		for(Ability a : Ability.values())
		{
			m += "\n";
			if(sheet.hasSavingProficiency(a))
			{
				m += "__" + WordUtil.capitalize(a.name()) + ": " + sheet.getAbilityScore(a) + " [" + sheet.getAbilityModifier(a) + "]" + "__";
			}
			else
				m += WordUtil.capitalize(a.name()) + ": " + sheet.getAbilityScore(a) + " [" + sheet.getAbilityModifier(a) + "]";
		}
		this.append(m);
	}
	
}
