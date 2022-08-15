package com.atelier.discord.listeners;

import org.slf4j.Logger;

import com.atelier.AtelierBot;
import com.atelier.database.AtelierDB;
import com.atelier.discord.User;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LoginListener extends ListenerAdapter {

	private Logger logger;
	private AtelierDB db = AtelierDB.getInstance();

	public LoginListener(AtelierBot bot) {
		logger = bot.getLogger();
	}

	@Override
	public void onGuildReady(GuildReadyEvent event) {
		Guild guild = event.getGuild();
		logger.info("Initialized Guild: " + guild.getName() + " [" + guild.getIdLong() + "]");
		
		for(Member member : guild.getMembers()) {
			User user = db.getUser(member.getIdLong());
			user.setName(member.getUser().getName());
		}
	}
}
