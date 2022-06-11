package com.tempera.atelier.discord.commands.character;

import java.awt.Color;

import com.tempera.atelier.dnd.types.Sheet;
import com.tempera.atelier.dnd.types.character.CharacterAttribute;
import com.tempera.atelier.dnd.types.enums.Ability;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

public class CharacterInfoMessageBuilder extends MessageBuilder {

	public CharacterInfoMessageBuilder(Sheet sheet) {
		StringBuilder b = new StringBuilder();
		EmbedBuilder eb = new EmbedBuilder();
		String prefix;

		eb.setColor(Color.GREEN);
		eb.setTitle("Character Info");

		b.append("Name: " + sheet.getName() + "\n");
		if(sheet.getClasses().size() > 0)
			b.append("Class: " + sheet.getClasses().get(0));
		eb.addField("Info", b.toString(), true);

		b.setLength(0);
		prefix = "";
		for (Ability a : Ability.values()) {
			b.append(prefix);
			b.append(String.format(
				sheet.hasSavingProficiency(a) ? "__%s: %s [%d]__" : "%s: %s [%d]",
				a, sheet.getAbilityScore(a), sheet.getAbilityModifier(a)));
			prefix = "\n";
		}
		eb.addField("Abilities", b.toString(), true);

		b.setLength(0);
		prefix = "";
		for (CharacterAttribute a : sheet.getAttributes()) {
			b.append(prefix);
			b.append(a.getName());
			prefix = "\n";
		}
		if(sheet.getAttributes().size() > 0)
			eb.addField("Attributes", b.toString(), true);

		setEmbeds(eb.build());
	}

}
