package com.rithsagea.atelier.discord.music;

import java.nio.ByteBuffer;
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

import net.dv8tion.jda.api.audio.AudioSendHandler;

public class AtelierAudioHandler extends AudioEventAdapter implements AudioLoadResultHandler, AudioSendHandler {

	private BlockingQueue<AudioTrack> queue;
	private AudioPlayerManager manager;
	private AudioPlayer player;
	private AudioFrame frame;
	
	public AtelierAudioHandler(AudioPlayerManager manager, AudioPlayer player) {
		this.manager = manager;
		this.player = player;
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
	
	public void nextTrack() {
		player.startTrack(queue.poll(), false);
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
		queue(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		playlist.getTracks().forEach(this::queue);
	}

	@Override
	public void noMatches() {
		//TODO tell user we have nothing
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		//TODO tell user stuff broke
	}

}
