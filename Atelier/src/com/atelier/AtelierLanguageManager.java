package com.atelier;

import com.atelier.discord.commands.AbstractInteraction;
import com.atelier.discord.commands.AbstractInteraction.AbstractCommand;
import com.atelier.discord.commands.AbstractInteraction.AbstractSubcommand;
import com.atelier.discord.commands.AbstractInteraction.AbstractSubcommandGroup;
import com.rithsagea.util.lang.LanguageManager;
import com.rithsagea.util.lang.TemplateMessage;

public class AtelierLanguageManager extends LanguageManager {
	private static final AtelierLanguageManager INSTANCE = new AtelierLanguageManager();
	public static AtelierLanguageManager getInstance() { return INSTANCE; }
	private AtelierLanguageManager() {}
	
	public String get(Object obj, String key) {
		String group = obj.getClass().getSimpleName();
		String name = "";
		
		if(obj instanceof AbstractInteraction) {
			name = group;
			if(obj instanceof AbstractCommand) group = "Command";
			else if(obj instanceof AbstractSubcommand) group = "Subcommand";
			else if(obj instanceof AbstractSubcommandGroup) group = "SubcommandGroup";
			else group = "Interaction";
		}
		
		return get(String.format("%s.%s.%s", group, name, key));
	}
	
	public TemplateMessage getMessage(Object obj, String key) {
		return new TemplateMessage(get(obj, key));
	}
}
