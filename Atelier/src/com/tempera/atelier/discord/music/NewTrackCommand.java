package com.tempera.atelier.discord.music;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Track;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NewTrackCommand extends MusicSubCommand {

	private AtelierDB db;

	public NewTrackCommand(AtelierBot bot) {
		super(bot.getAudioManager());
		db = bot.getDatabase();
	}

	@Override
	public String getLabel() {
		return "new";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.ADMINISTRATOR;
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, List<String> args, MessageReceivedEvent event) {
		Track track = new Track();
		db.addTrack(track);

		event.getChannel()
			.sendMessageFormat("Created new track %s", track.getId())
			.queue();
	}
}


