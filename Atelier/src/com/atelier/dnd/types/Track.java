package com.atelier.dnd.types;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.atelier.database.Id;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class Track {
	@Id
	private final UUID id;
	private Set<AudioTrack> tracks = new HashSet<>();
	
	public Track(UUID id) {
		this.id = id;
	}
	
	public Track() {
		this(UUID.randomUUID());
	}
	
	public void addTrack(AudioTrack track) {
		tracks.add(track);
	}
	
	public void removeTrack(AudioTrack track) {
		tracks.remove(track);
	}
	
	public UUID getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return String.format("%s", id);
	}
	
	public Set<AudioTrack> getTracks() {
		return Collections.unmodifiableSet(tracks);
	}
}
