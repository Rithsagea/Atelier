package com.tempera.atelier.discord;

import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.data.DataObject;

public abstract class SlashBaseCommand implements SlashAbstractCommand {

	private String label;
	private String description;
	
	public SlashBaseCommand(String label, String description) {
		this.label = label;
		this.description = description;
	}
	
	@Override
	public final String getName() {
		return label;
	}
	
	@Override
	public final String getDescription() {
		return description;
	}
	
	@Override
	public final DataObject getData() {
		return Commands.slash(label, description).toData();
	}
}
