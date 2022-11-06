package com.atelier;

import com.atelier.console.AbstractConsoleCommand;
import com.atelier.console.AbstractConsoleGroupCommand;
import com.atelier.console.AbstractConsoleSubcommand;
import com.atelier.discord.commands.AbstractInteraction;
import com.atelier.discord.commands.AbstractInteraction.AbstractCommand;
import com.atelier.discord.commands.AbstractInteraction.AbstractSubcommand;
import com.atelier.discord.commands.AbstractInteraction.AbstractSubcommandGroup;
import com.atelier.dnd.Ability;
import com.atelier.dnd.Skill;
import com.atelier.dnd.character.CharacterAttribute;
import com.atelier.dnd.character.CharacterClass;
import com.atelier.dnd.character.CharacterRace;
import com.atelier.dnd.embeds.AtelierEmbedBuilder;
import com.rithsagea.util.lang.LanguageManager;

public class AtelierLanguageManager extends LanguageManager {
	private static final AtelierLanguageManager INSTANCE = new AtelierLanguageManager();
	public static AtelierLanguageManager getInstance() { return INSTANCE; }
	private AtelierLanguageManager() {}
	
	public String get(Object obj, String key) {
		String group = obj.getClass().getSimpleName();
		String name = "";
		
		if(obj instanceof Ability) name = ((Ability) obj).name();
		else if(obj instanceof Skill) name = ((Skill) obj).name();
		else name = group;

		if(obj instanceof AtelierEmbedBuilder) {
			group = "Embed";
		}
		
		if(obj instanceof AbstractInteraction) {
			if(obj instanceof AbstractCommand) group = "Command";
			else if(obj instanceof AbstractSubcommand) group = "Subcommand";
			else if(obj instanceof AbstractSubcommandGroup) group = "SubcommandGroup";
			else group = "Interaction";
		}
		
		if(obj instanceof AbstractConsoleCommand) {
			if(obj instanceof AbstractConsoleSubcommand) group = "ConsoleSubcommand";
			else if(obj instanceof AbstractConsoleGroupCommand) group = "ConsoleCommandGroup";
			else group = "ConsoleCommand";
		}

		if(obj instanceof CharacterClass) group = "CharacterClass";

		if(obj instanceof CharacterRace) group = "CharacterRace";

		if(obj instanceof CharacterAttribute) group = "CharacterAttribute";

		return get(String.format("%s.%s.%s", group, name, key));
	}
}
