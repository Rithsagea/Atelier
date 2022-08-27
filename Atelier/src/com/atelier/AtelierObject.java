package com.atelier;

public interface AtelierObject {
	public default String getProperty(String key) {
		return AtelierLanguageManager.getInstance().get(this, key);
	}
}
