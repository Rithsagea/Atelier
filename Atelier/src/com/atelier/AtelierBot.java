package com.atelier;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atelier.discord.CommandListener;
import com.atelier.discord.LoginListener;
import com.atelier.discord.MessageListener;
import com.atelier.discord.commands.music.AtelierAudioManager;
import com.atelier.dnd.types.AtelierDB;
import com.atelier.dnd.types.DBSaveTask;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class AtelierBot {

	private AtelierDB db;
	private Config config;

	private Logger logger = LoggerFactory.getLogger("Atelier");
	private JDA jda;
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

	private boolean running;

	public AtelierBot(String configPath) {
		System.setProperty("http.agent", "Chrome");

		this.config = Config.init(configPath);
		this.db = AtelierDB.init(config);
	}

	public void init() {
		running = true;
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
		jda.addEventListener(new MessageListener());
		jda.addEventListener(new CommandListener(this));
	}

	private void registerTasks() {
		scheduler.scheduleAtFixedRate(new DBSaveTask(db), 15, 300, TimeUnit.SECONDS);
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

	public void stop() {
		running = false;
		jda.shutdownNow();
		AtelierAudioManager.getInstance().shutdown();
		db.disconnect();

		System.exit(0);
	}

	public boolean isRunning() {
		return running;
	}
}
