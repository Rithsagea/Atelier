package com.tempera.atelier.discord;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
	private UUID campaignId;

	private PermissionLevel level;

	private UUID editingSheetId;
	private UUID editingCampaignId;
	
	private Set<UUID> sheets = new HashSet<>();
	
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
	
	public String getTag() {
		return String.format("<@%d>", id);
	}

	public UUID getSheetId() {
		return sheetId;
	}

	public UUID getCampaignId() {
		return campaignId;
	}
	
	public PermissionLevel getLevel() {
		return level;
	}

	public UUID getSelectedSheetId() {
		return editingSheetId;
	}
	
	public UUID getSelectedCampaignId() {
		return editingCampaignId;
	}
	
	public Set<UUID> getSheets() {
		return Collections.unmodifiableSet(sheets);
	}

	// MUTATORS

	public void setName(String name) {
		this.name = name;
	}

	public void setSheet(Sheet sheet) {
		sheetId = sheet.getId();
	}

	public void setCampaign(Campaign campaign) {
		campaignId = campaign.getId();
	}
	
	public void setLevel(PermissionLevel level) {
		this.level = level;
	}

	public void setEditingSheet(Sheet sheet) {
		editingSheetId = sheet.getId();
	}
	
	public void setEditingCampaign(Campaign campaign) {
		editingCampaignId = campaign.getId();
	}
	
	public void addSheet(Sheet sheet) {
		sheets.add(sheet.getId());
	}
	
	public boolean removeSheet(Sheet sheet) {
		return sheets.remove(sheet.getId());
	}
}
