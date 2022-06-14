package com.tempera.atelier.discord.music;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.dnd.types.AtelierDB;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SaveTrackCommand extends MusicSubCommand{
	private AtelierDB db;

	public SaveTrackCommand(AtelierBot bot) {
		super(bot.getAudioManager(), "", "");
		db = bot.getDatabase();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
	/**	AudioPlayerManager manager = audioHandler.getManager();
		if (args.size() < 2) {
			event.getChannel().sendMessage("Format: save [Track ID] [SongURL]").queue();
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
				event.getChannel().sendMessage("Saved playlist")
				.queue();
			}

			@Override
			public void noMatches() {
				event.getChannel().sendMessage("No matches!").queue();
			}

			@Override
			public void loadFailed(FriendlyException throwable) {}
		});**/
	}

}
