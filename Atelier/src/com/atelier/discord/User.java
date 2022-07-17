package com.atelier.discord;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.atelier.database.AtelierDB;
import com.atelier.database.Id;
import com.atelier.dnd.types.Campaign;
import com.atelier.dnd.types.Sheet;

public class User {

	@Id
	private final long id;

	private String name;
	private UUID sheetId;
	private UUID campaignId;

	private UUID editingSheetId;
	private UUID editingCampaignId;
	
	private Set<UUID> sheets = new HashSet<>();
	
	public User(long id) {
		this.id = id;
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

	public Sheet getSheet() {
		return AtelierDB.getInstance().getSheet(sheetId);
	}

	public Campaign getCampaign() {
		return AtelierDB.getInstance().getCampaign(campaignId);
	}

	public UUID getSelectedSheetId() {
		return editingSheetId;
	}
	
	public UUID getSelectedCampaignId() {
		return editingCampaignId;
	}
	
	public Set<Sheet> getSheets() {
		AtelierDB db = AtelierDB.getInstance();
		return sheets.stream().map(db::getSheet).collect(Collectors.toSet());
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
