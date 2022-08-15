package com.atelier;

import com.rithsagea.util.lang.LanguageManager;

public class AtelierLanguageManager extends LanguageManager {
	private static final AtelierLanguageManager INSTANCE = new AtelierLanguageManager();
	public static AtelierLanguageManager getInstance() { return INSTANCE; }
	private AtelierLanguageManager() {}
	
	public String get(Object obj, String key) {
		return null;
	}
}
