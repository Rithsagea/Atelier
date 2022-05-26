package com.tempera.atelier.discord;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.MessageChannel;

public class MenuManager {
	private Map<Long, Menu> menus;
	
	public MenuManager() {
		menus = new HashMap<>();
	}
	
	public void addMenu(MessageChannel channel, Menu menu) {
		channel.sendMessage(menu.initialize()).queue(m -> {
			menus.put(m.getIdLong(), menu);
		});
	}
	
	public Menu getMenu(long id) {
		return menus.get(id);
	}
	
	public Menu removeMenu(long id) {
		return menus.remove(id);
	}
}
