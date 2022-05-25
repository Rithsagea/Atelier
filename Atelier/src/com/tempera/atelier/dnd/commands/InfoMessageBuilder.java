package com.tempera.atelier.dnd.commands;

import java.awt.Color;

import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.types.character.Attribute;
import com.tempera.atelier.dnd.types.enums.Ability;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

public class InfoMessageBuilder extends MessageBuilder{
	
	public InfoMessageBuilder(Sheet sheet) {
		StringBuilder b = new StringBuilder();
		EmbedBuilder eb = new EmbedBuilder();
		String prefix;
		
		eb.setColor(Color.GREEN);
		eb.setTitle("Character Info");
		
		b.append("Name: " + sheet.getName() + "\n");
		b.append("Class: " + sheet.getClasses().get(0));
		eb.addField("Info", b.toString(), false);
		
		b.setLength(0);
		prefix = "";
		for(Ability a : Ability.values()) {
			b.append(prefix);
			b.append(String.format(sheet.hasSavingProficiency(a) ? "__%s: %s [%d]__" : "%s: %s [%d]", 
					a, sheet.getAbilityScore(a), sheet.getAbilityModifier(a)));
			prefix = "\n";
		}
		eb.addField("Abilities", b.toString(), false);
		
		b.setLength(0);
		prefix = "";
		for(Attribute a : sheet.getClasses().get(0).getAttributes()) {
			b.append(prefix);
			b.append(prefix + a.getName());
			prefix = "\n";
		}
		eb.addField("Attributes", b.toString(), false);
		
		setEmbeds(eb.build());
	}
	
}
