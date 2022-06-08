package com.tempera.atelier.discord;

import java.util.UUID;

import org.mongojack.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.Campaign;
import com.tempera.atelier.dnd.types.Sheet;

public class User {

	@Id
	private final long id;

	private String name;
	private UUID sheetId;

	private PermissionLevel level;

	private UUID selectedSheetId;
	private UUID selectedCampaignId;

	@JsonCreator
	public User(@Id long id) {
		this.id = id;

		level = PermissionLevel.USER;
	}

	@Override
	public String toString() {
		return String.format("%s [%d]", name, id);
	}

	// ACCESSORS

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public UUID getSheetId() {
		return sheetId;
	}

	public PermissionLevel getLevel() {
		return level;
	}

	public UUID getSelectedSheetId() {
		return selectedSheetId;
	}
	
	public UUID getSelectedCampaignId() {
		return selectedCampaignId;
	}

	// MUTATORS

	public void setName(String name) {
		this.name = name;
	}

	public void setSheetId(UUID id) {
		sheetId = id;
	}

	public void setLevel(PermissionLevel level) {
		this.level = level;
	}

	public void setSelectedSheet(Sheet sheet) {
		selectedSheetId = sheet.getId();
	}
	
	public void setSelectedCampaign(Campaign campaign) {
		selectedCampaignId = campaign.getId();
	}
}
