package com.atelier;

import com.atelier.discord.commands.AbstractInteraction;
import com.atelier.discord.commands.AbstractInteraction.AbstractCommand;
import com.atelier.discord.commands.AbstractInteraction.AbstractSubcommand;
import com.atelier.discord.commands.AbstractInteraction.AbstractSubcommandGroup;
import com.rithsagea.util.lang.LanguageManager;

public class AtelierLanguageManager extends LanguageManager {
	private static final AtelierLanguageManager INSTANCE = new AtelierLanguageManager();
	public static AtelierLanguageManager getInstance() { return INSTANCE; }
	private AtelierLanguageManager() {}
	
	public String get(Object obj, String key) {
		String group = obj.getClass().getSimpleName();
		String name = "";
		
		if(obj instanceof AbstractInteraction) {
			name = group;
			group = "Interaction";
			if(obj instanceof AbstractCommand) group = "Command";
			if(obj instanceof AbstractSubcommand) group = "Subcommand";
			if(obj instanceof AbstractSubcommandGroup) group = "SubcommandGroup";
		}
		
		return get(String.format("%s.%s.%s", group, name, key));
	}
}
