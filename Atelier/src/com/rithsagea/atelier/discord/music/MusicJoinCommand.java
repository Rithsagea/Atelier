package com.rithsagea.atelier.discord.music;

import java.util.List;

import com.rithsagea.atelier.discord.AtelierCommand;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.User;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class MusicJoinCommand implements AtelierCommand {

	private AtelierAudioManager audioManager;
	
	public MusicJoinCommand(AtelierAudioManager audioManager) {
		this.audioManager = audioManager;
	}
	
	@Override
	public String getLabel() {
		return "join";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}
	
	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		
		Guild guild = event.getGuild();
		AudioManager manager = guild.getAudioManager();
		
		GuildVoiceState state = event.getMember().getVoiceState();
		if(state == null) return;
		
		AudioChannel channel = state.getChannel();
		if(channel == null) return;
		
		manager.setSendingHandler(audioManager.getAudioHandler(guild));
		manager.openAudioConnection(channel);
	}

}
