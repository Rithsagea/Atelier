package com.tempera.atelier.discord.music;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class AtelierAudioHandler extends AudioEventAdapter
	implements AudioLoadResultHandler, AudioSendHandler {

	private BlockingQueue<AudioTrack> queue;
	private AudioPlayerManager manager;
	private AudioPlayer player;
	private AudioFrame frame;
	private MessageChannel channel;
	private boolean looping;

	public AtelierAudioHandler(AudioPlayerManager manager, AudioPlayer player,
		MessageReceivedEvent event) {
		this.manager = manager;
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
		this.looping = false;

		channel = event.getChannel();

		player.addListener(this);
	}

	public boolean toggleLoop() {
		if (looping)
			return looping = false;
		return looping = true;
	}

	public AudioChannel joinVc(MessageReceivedEvent event) {
		if (event.getAuthor()
			.isBot())
			return null;

		Guild guild = event.getGuild();
		AudioManager manager = guild.getAudioManager();
		GuildVoiceState state = event.getMember()
			.getVoiceState();
		if (state == null)
			return null;

		AudioChannel channel = state.getChannel();
		if (channel == null) {
			event.getChannel()
				.sendMessage("Not in a voice channel!")
				.queue();
			return null;
		}
		
		manager.setSendingHandler(this);
		manager.openAudioConnection(channel);

		event.getChannel()
			.sendMessage(
				String.format("Joined channel `[%s]`", channel.getName()))
			.queue();
		return channel;
	}

	public void loadTrack(String identifier) {
		manager.loadItem(identifier, this);
	}

	public void queue(AudioTrack track) {
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	public AudioTrack getPlayingTrack() {
		return player.getPlayingTrack();
	}

	public void nextTrack() {
		player.startTrack(queue.poll(), false);
	}

	public List<AudioTrack> getQueue() {
		return Collections.unmodifiableList(new ArrayList<>(queue));
	}

	/// AUDIO EVENT ADAPTER

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track,
		AudioTrackEndReason endReason) {
		if (looping)
			this.loadTrack(track.getIdentifier());
		if (endReason.mayStartNext) {
			nextTrack();
		}
	}

	/// AUDIO SEND HANDLER

	@Override
	public boolean canProvide() {
		frame = player.provide();
		return frame != null;
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		return ByteBuffer.wrap(frame.getData());
	}

	@Override
	public boolean isOpus() {
		return true;
	}

	/// AUDIO LOAD RESULT HANDLER

	@Override
	public void trackLoaded(AudioTrack track) {
		if (!looping) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.GREEN);
			eb.setTitle(String.format("Added - `%s`", track.getInfo().title));
			channel.sendMessageEmbeds(eb.build())
				.queue();
		}

		queue(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setTitle(String.format("Added %d songs", playlist.getTracks()
			.size()));
		channel.sendMessageEmbeds(eb.build())
			.queue();

		playlist.getTracks()
			.forEach(this::queue);
	}

	@Override
	public void noMatches() {
		channel.sendMessage("No matches!")
			.queue();
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		channel.sendMessage("Something unexpected happened!")
			.queue();
	}

}
