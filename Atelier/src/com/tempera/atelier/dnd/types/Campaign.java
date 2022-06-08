package com.tempera.atelier.dnd.types;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.mongojack.Id;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Campaign {
	@Id
	private final UUID id;
	private Set<Sheet> sheets = new HashSet<>();
	
	@JsonCreator
	public Campaign(@Id UUID id) {
		this.id = id;
	}
	
	public Campaign() {
		this(UUID.randomUUID());
	}
	
	public void addSheet(Sheet sheet) {
		sheets.add(sheet);
	}
	
	public void removeSheet(Sheet sheet) {
		sheets.remove(sheet);
	}
	
	public UUID getId() {
		return id;
	}
	
	public Set<Sheet> getSheets() {
		return Collections.unmodifiableSet(sheets);
	}
}
