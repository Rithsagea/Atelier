package com.tempera.atelier.discord.music;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AtelierAudioHandler extends AudioEventAdapter implements AudioLoadResultHandler, AudioSendHandler {

	private BlockingQueue<AudioTrack> queue;
	private AudioPlayerManager manager;
	private AudioPlayer player;
	private AudioFrame frame;
	private MessageReceivedEvent event;
	
	public AtelierAudioHandler(AudioPlayerManager manager, AudioPlayer player, MessageReceivedEvent event) {
		this.manager = manager;
		this.player = player;
		this.event = event;
		this.queue = new LinkedBlockingQueue<>();
		
		player.addListener(this);
	}
	
	public void loadTrack(String identifier) {
		manager.loadItem(identifier, this);
	}
	
	public void queue(AudioTrack track) {
		if(!player.startTrack(track, true)) {
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
		return queue.stream().collect(Collectors.toList());
	}
	
	/// AUDIO EVENT ADAPTER
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if(endReason.mayStartNext) {
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
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setTitle(String.format("Added - `%s`", track.getInfo().title));
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
		queue(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setTitle(String.format("Added %d songs", playlist.getTracks().size()));
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
		playlist.getTracks().forEach(this::queue);
	}

	@Override
	public void noMatches() {
		event.getChannel().sendMessage("No matches!").queue();
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		event.getChannel().sendMessage("Something unexpected happened!").queue();
	}

}
