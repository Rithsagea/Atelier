package com.rithsagea.atelier.discord.music;

import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.commands.AtelierCommand;
import com.rithsagea.atelier.discord.commands.PermissionLevel;
import com.rithsagea.atelier.dnd.User;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class MusicCommand implements AtelierCommand {

	private AtelierAudioManager audioManager;
	
	public MusicCommand(AtelierBot bot) {
		audioManager = bot.getAudioManager();
	}
	
	@Override
	public String getLabel() {
		return "music";
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
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		Guild guild = event.getGuild();
		AtelierAudioHandler audioHandler = audioManager.getAudioHandler(guild);
		
		switch(args.get(1)) {
			
		case "join":
			AudioManager manager = guild.getAudioManager();
			GuildVoiceState state = event.getMember().getVoiceState();
			if(state == null) return;
			AudioChannel channel = state.getChannel();
			if(channel == null) return;
			manager.setSendingHandler(audioHandler);
			manager.openAudioConnection(channel);
			break;
		
		case "play":
			audioHandler.loadTrack(args.get(2));
			break;
		
		case "queue":
			EmbedBuilder eb = new EmbedBuilder();
			String msg = "";
			for (int i = 0; i < audioHandler.getQueue().size(); i++) {
				msg = msg.concat((i+1) + " - " + audioHandler.getQueue().get(i).getInfo().title + "\n");
			}
			if (msg == "")
				msg = "No song in queue!";
			eb.addField("Queue:", msg, true);
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			break;
		}
	}

}
