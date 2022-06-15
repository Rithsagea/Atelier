package com.tempera.test;

import javax.security.auth.login.LoginException;

import com.tempera.atelier.Config;
import com.tempera.atelier.discord.SlashCommandListener;
import com.tempera.atelier.dnd.types.AtelierDB;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class CommandTest {
	public static void main(String[] args) {
		System.setProperty("http.agent", "Chrome");

		Config config = Config.init("config.properties");
		AtelierDB.init(config);
		
		JDABuilder builder = JDABuilder.createDefault(config.getDiscordToken());
		JDA jda;
		try {
			jda = builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		new SlashCommandListener(null);
	}
}
