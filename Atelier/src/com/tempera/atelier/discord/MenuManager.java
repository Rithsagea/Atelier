package com.tempera.atelier.discord;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {
	private Map<Long, Menu> menus;
	
	public MenuManager() {
		menus = new HashMap<>();
	}
	
	public void addMenu(long id, Menu menu) {
		menus.put(id, menu);
	}
	
	public Menu getMenu(long id) {
		return menus.get(id);
	}
	
	public Menu removeMenu(long id) {
		return menus.remove(id);
	}
}
