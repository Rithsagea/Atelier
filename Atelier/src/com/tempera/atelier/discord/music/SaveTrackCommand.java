package com.tempera.atelier.discord.music;

import java.util.List;
import java.util.UUID;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.acommands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Track;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SaveTrackCommand extends MusicSubCommand{
	private AtelierDB db;

	public SaveTrackCommand(AtelierBot bot) {
		super(bot.getAudioManager());
		db = bot.getDatabase();
	}

	@Override
	public String getLabel() {
		return "save";
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
		AudioPlayerManager manager = audioHandler.getManager();
		if (args.size() < 2) {
			event.getChannel().sendMessage("Specify a valid track id").queue();
			return;
		}
		if (args.size() < 3) {
			event.getChannel().sendMessage("Missing song parameter").queue();
			return;
		}

		UUID id;

		try {
			id = UUID.fromString(args.get(1));
		} catch (IllegalArgumentException e) {
			event.getChannel().sendMessage("Invalid id format").queue();
			return;
		}
		
		Track t = db.getTrack(id);
		manager.loadItem(args.get(2), new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				t.addTrack(track);
				event.getChannel().sendMessage(String.format("Saved `%s`", track.getInfo().title))
				.queue();
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				playlist.getTracks().forEach(t::addTrack);
			}

			@Override
			public void noMatches() {
				event.getChannel().sendMessage("No matches!").queue();
			}

			@Override
			public void loadFailed(FriendlyException throwable) {}
		});
	}

}
