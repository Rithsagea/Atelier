package com.tempera.atelier.discord.music;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Track;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class NewTrackCommand extends MusicSubCommand {

	private AtelierDB db;

	public NewTrackCommand(AtelierBot bot) {
		super(bot.getAudioManager(), "", "");
		db = bot.getDatabase();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		Track track = new Track();
		db.addTrack(track);

		event.getChannel()
			.sendMessageFormat("Created new track %s", track.getId())
			.queue();
	}
}


