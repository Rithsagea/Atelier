package com.tempera.atelier.discord.music;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.lcommands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Track;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ListTrackCommand extends MusicSubCommand{
	private AtelierDB db;
	
	public ListTrackCommand(AtelierBot bot) {
		super(bot.getAudioManager());
		db = bot.getDatabase();
	}

	@Override
	public String getLabel() {
		return "list";
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
