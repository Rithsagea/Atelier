package com.tempera.atelier.discord;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashMenuManager {
	private static final SlashMenuManager INSTANCE = new SlashMenuManager();
	public static SlashMenuManager getInstance() {
		return INSTANCE;
	}
	
	private Map<Long, SlashMenu> menus = new HashMap<>();

	private SlashMenuManager() {
		
	}
	
	public void addMenu(SlashMenu menu, boolean ephemeral, SlashCommandInteractionEvent event) {
		event.deferReply(ephemeral).queue(hook ->
			hook.editOriginal(menu.initialize())
			.queue(msg -> menus.put(msg.getIdLong(), menu)));
	}
	
	public SlashMenu getMenu(long id) {
		return menus.get(id);
	}
}
