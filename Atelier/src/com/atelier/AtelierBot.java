package com.atelier;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atelier.console.AtelierConsole;
import com.atelier.database.AtelierDB;
import com.atelier.database.DBSaveTask;
import com.atelier.discord.commands.music.AtelierAudioManager;
import com.atelier.discord.listeners.CommandListener;
import com.atelier.discord.listeners.LoginListener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class AtelierBot {

	private static AtelierBot INSTANCE;
	public static AtelierBot init(String configPath) {
		return INSTANCE = new AtelierBot(configPath);
	}
	
	public static AtelierBot getInstance() {
		return INSTANCE;
	}
	
	private AtelierDB db;
	private AtelierConsole console;
	private Config config;

	private Logger logger = LoggerFactory.getLogger("Atelier");
	private JDA jda;
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

	private boolean running;

	private AtelierBot(String configPath) {
		System.setProperty("http.agent", "Chrome");

		this.config = Config.init(configPath);
		this.db = AtelierDB.init(config);
		console = new AtelierConsole(System.in);
	}

	public void start() {
		running = true;
		logger.info("Initializing Atelier");
		
		JDABuilder builder = JDABuilder.createDefault(config.getDiscordToken());
		try {
			jda = builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		console.start();

		registerTasks();

		jda.addEventListener(new LoginListener(this));
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
		scheduler.shutdown();
		
		AtelierAudioManager.getInstance().shutdown();
		jda.shutdownNow();
		db.disconnect();
		
		try {
			scheduler.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public boolean isRunning() {
		return running;
	}
}
