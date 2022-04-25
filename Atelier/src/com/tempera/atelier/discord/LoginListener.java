package com.tempera.atelier.discord;

import org.slf4j.Logger;

import com.tempera.atelier.AtelierBot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LoginListener extends ListenerAdapter {
	
	private Logger logger;
	
	public LoginListener(AtelierBot bot) {
		logger = bot.getLogger();
	}
	
	@Override
	public void onGuildReady(GuildReadyEvent event) {
		Guild guild = event.getGuild();
		logger.info("Initialized Guild: " + guild.getName() + " [" + guild.getIdLong() + "]");
	}
}
