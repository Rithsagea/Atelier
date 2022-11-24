package com.atelier.database;

import com.atelier.util.AtelierLanguageManager;
import com.atelier.util.TemplateMessage;

public interface AtelierObject {
	public default String getProperty(String key) {
		return AtelierLanguageManager.getInstance().get(this, key);
	}
	
	public default TemplateMessage getMessage(String key) {
		return new TemplateMessage(getProperty("msg." + key));
	}

	public default TemplateMessage getError(String key) {
		return new TemplateMessage(getProperty("error." + key));
	}
}
