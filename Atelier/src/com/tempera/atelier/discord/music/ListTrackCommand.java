package com.tempera.atelier.discord.music;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Track;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ListTrackCommand extends MusicSubCommand{
	private AtelierDB db;
	
	public ListTrackCommand(AtelierBot bot) {
		super(bot.getAudioManager() , "", "");
		db = bot.getDatabase();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		MessageBuilder b = new MessageBuilder();
		for (Track track : db.listTracks()) {
			b.append(track);
			b.append("\n");
		}
		event.getChannel()
			.sendMessage(b.build())
			.queue();
	}
}
