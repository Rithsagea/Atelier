package com.atelier;

public interface AtelierObject {
	public default String getProperty(String key) {
		return AtelierLanguageManager.getInstance().get(this, key);
	}
	
	public default TemplateMessage getMessage(String key) {
		return new TemplateMessage(getProperty("msg." + key));
	}
}
