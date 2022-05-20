package com.tempera.atelier.dnd.commands;

import java.awt.Color;

import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.types.character.Attribute;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.util.WordUtil;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

public class InfoMessageBuilder extends MessageBuilder{
	
	private Sheet sheet;
	
	public InfoMessageBuilder(Sheet sheet)
	{
		this.sheet = sheet;
		String m = "";
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
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
		for(Attribute a : sheet.getClasses().get(0).getAttributes())
		{
			m += "\n";
			m += a.getName();
		}
		eb.setTitle("Character Info");
		eb.addField("Info", m, true);
		setEmbeds(eb.build());
	}
	
}
