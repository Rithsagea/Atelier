package com.rithsagea.util.lang;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

public class LanguageManager {

	private Locale locale = Locale.US;
	private Properties messages;
	private Set<String> missing;

	public LanguageManager() {
		reloadMessages();
	}

	private void reloadMessages() {
		messages = new Properties();
		missing = new LinkedHashSet<>();

		Stream.of("console", "discord", "dnd").forEach(
			s -> {
				try {
					messages.load(ClassLoader.getSystemResourceAsStream(String.format("lang/%s/%s.properties", locale, s)));
				} catch(IOException e) {
					e.printStackTrace();
				}
			});
	}

	public Locale getLocale() {
		return locale;
	}

	public String get(String key) {
		if (!messages.containsKey(key)) {
			missing.add(key + "=");
			return key;
		}
		return messages.getProperty(key);
	}

	public Set<String> getMissing() {
		return Collections.unmodifiableSet(missing);
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		reloadMessages();
	}
}
