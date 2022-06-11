package com.tempera.atelier.discord;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MenuManager {
	private static final MenuManager INSTANCE = new MenuManager();
	public static MenuManager getInstance() {
		return INSTANCE;
	}
	
	private Map<Long, Menu> menus;

	private MenuManager() {
		menus = new HashMap<>();
	}

	public void addMenu(MessageChannel channel, Menu menu) {
		channel.sendMessage(menu.initialize())
			.queue(m -> {
				menus.put(m.getIdLong(), menu);
			});
	}
	
	public void addMenu(SlashCommandInteractionEvent event, Menu menu) {
		event.reply(menu.initialize()).queue();
	}

	public Menu getMenu(long id) {
		return menus.get(id);
	}

	public Menu removeMenu(long id) {
		return menus.remove(id);
	}
}
