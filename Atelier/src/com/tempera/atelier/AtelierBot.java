package com.tempera.atelier;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tempera.atelier.discord.LoginListener;
import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.MessageListener;
import com.tempera.atelier.discord.acommands.CommandRegistry;
import com.tempera.atelier.discord.music.AtelierAudioManager;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.DBSaveTask;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class AtelierBot {

	private AtelierDB db;
	private Config config;

	private Logger logger;
	private JDA jda;
	private ScheduledExecutorService scheduler;

	private AtelierAudioManager audioManager;
	private CommandRegistry commandRegistry;
	private MenuManager menuManager;

	private boolean running;

	public AtelierBot(String configPath) {
		System.setProperty("http.agent", "Chrome");

		this.config = new Config(configPath);

		db = AtelierDB.init(config);
		commandRegistry = new CommandRegistry();
		menuManager = MenuManager.getInstance();
		logger = LoggerFactory.getLogger("Atelier");
		running = true;

		scheduler = Executors.newScheduledThreadPool(10);

		audioManager = new AtelierAudioManager();
	}

	public void init() {
		logger.info("Initializing Atelier");

		JDABuilder builder = JDABuilder.createDefault(config.getDiscordToken());
		try {
			jda = builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
			System.exit(0);
		}

		registerTasks();

		jda.addEventListener(new LoginListener(this));
		jda.addEventListener(new MessageListener(this));
	}

	private void registerTasks() {
		scheduler.scheduleAtFixedRate(new DBSaveTask(db), 15, 300,
			TimeUnit.SECONDS);
	}

	public Config getConfig() {
		return config;
	}

	public Logger getLogger() {
		return logger;
	}
	
	public JDA getJda() {
		return jda;
	}

	public AtelierDB getDatabase() {
		return db;
	}

	public CommandRegistry getCommandRegistry() {
		return commandRegistry;
	}

	public AtelierAudioManager getAudioManager() {
		return audioManager;
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}

	public void stop() {
		running = false;
		jda.shutdownNow();
		audioManager.shutdown();
		db.disconnect();

		System.exit(0);
	}

	public boolean isRunning() {
		return running;
	}
}
