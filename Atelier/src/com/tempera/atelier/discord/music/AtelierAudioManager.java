package com.tempera.atelier.discord.music;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AtelierAudioManager {
	private AudioPlayerManager playerManager;
	private Map<Guild, AtelierAudioHandler> audioHandlers;
	
	public AtelierAudioManager() {
		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		
		audioHandlers = new HashMap<>();
	}
	
	public void shutdown() {
		playerManager.shutdown();
	}
	
	public AtelierAudioHandler getAudioHandler(MessageReceivedEvent event) {
		Guild guild = event.getGuild();
		if(!audioHandlers.containsKey(guild)) {
			audioHandlers.put(guild, new AtelierAudioHandler(playerManager, playerManager.createPlayer(), event));
		}
		
		return audioHandlers.get(guild);
	}
}
