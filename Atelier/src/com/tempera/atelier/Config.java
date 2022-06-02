package com.tempera.atelier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private File configFile;
	private Properties prop;

	private static final String DB_URL_KEY = "databaseUrl";
	private static final String DB_NAME = "databaseName";
	private static final String DISCORD_TOKEN = "discordToken";
	private static final String COMMAND_PREFIX = "commandPrefix";

	public Config(String configPath) {
		configFile = new File(configPath);
		prop = new Properties();

		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		load();
		setDefault(DB_URL_KEY, "");
		setDefault(DB_NAME, "atelier");
		setDefault(DISCORD_TOKEN, "");
		setDefault(COMMAND_PREFIX, "!");
		save();
	}

	private void setDefault(String key, String val) {
		if (!prop.containsKey(key)) {
			prop.setProperty(key, val);
		}
	}

	public void load() {
		try {
			prop.load(new FileReader(configFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			prop.store(new FileWriter(configFile), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDatabaseUrl() {
		return prop.getProperty(DB_URL_KEY);
	}

	public String getDatabaseName() {
		return prop.getProperty(DB_NAME);
	}

	public String getDiscordToken() {
		return prop.getProperty(DISCORD_TOKEN);
	}

	public String getCommandPrefix() {
		return prop.getProperty(COMMAND_PREFIX);
	}
}
