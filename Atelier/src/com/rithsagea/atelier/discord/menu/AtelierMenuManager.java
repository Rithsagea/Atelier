package com.rithsagea.atelier.discord.menu;

import java.util.HashMap;
import java.util.Map;

public class AtelierMenuManager {
	private Map<Long, AtelierMenu> menuTable;
	
	public AtelierMenuManager() {
		menuTable = new HashMap<>();
	}
	
	public void registerMenu(AtelierMenu menu) {
		menuTable.put(menu.getMessage().getIdLong(), menu);
	}
	
	public AtelierMenu getMenu(long id) {
		return menuTable.get(id);
	}
}
