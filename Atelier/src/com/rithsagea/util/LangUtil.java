package com.rithsagea.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LangUtil {
	private static Locale locale = Locale.ENGLISH;
	private static ResourceBundle resources;
	
	static {
		resources = ResourceBundle.getBundle("lang", locale);
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public static void setLocale(Locale locale) {
		LangUtil.locale = locale;
	}
	
	public String getMessage(String key) {
		return resources.getString(key);
	}
}
