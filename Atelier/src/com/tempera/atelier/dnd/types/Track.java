package com.tempera.atelier.dnd.types;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.mongojack.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class Track {
	@Id
	private final UUID id;
	private Set<AudioTrack> tracks = new HashSet<>();
	
	@JsonCreator
	public Track(@Id UUID id) {
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
	
	public Set<AudioTrack> getSheets() {
		return Collections.unmodifiableSet(tracks);
	}
}
