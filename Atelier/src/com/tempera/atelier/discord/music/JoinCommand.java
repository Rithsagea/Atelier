package com.tempera.atelier.discord.music;

import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.User;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand extends MusicSubCommand{

	public JoinCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}

	@Override
	public String getLabel() {
		return "join";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("m", "audio");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}
	
	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, List<String> args, MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		Guild guild = event.getGuild();
		AudioManager manager = guild.getAudioManager();
		GuildVoiceState state = event.getMember().getVoiceState();
		if(state == null) return;
		AudioChannel channel = state.getChannel();
		if(channel == null) return;
		manager.setSendingHandler(audioHandler);
		manager.openAudioConnection(channel);
	}

}
