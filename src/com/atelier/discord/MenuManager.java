package com.atelier.discord;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class MenuManager {
	private static final MenuManager INSTANCE = new MenuManager();
	public static MenuManager getInstance() {
		return INSTANCE;
	}
	
	private Map<Long, Menu> menus = new HashMap<>();

	private MenuManager() {}
	
	public void addMenu(Menu menu, boolean ephemeral, IReplyCallback event) {
		event.deferReply(ephemeral).queue(hook -> hook
			.editOriginal(menu.initialize())
			.queue(msg -> menus.put(msg.getIdLong(), menu)));
	}
	
	public Menu getMenu(long id) {
		return menus.get(id);
	}
}
