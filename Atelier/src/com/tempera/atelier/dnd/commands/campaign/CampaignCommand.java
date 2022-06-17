package com.tempera.atelier.dnd.commands.campaign;

import com.tempera.atelier.discord.commands.GroupCommand;

public class CampaignCommand extends GroupCommand {
	public CampaignCommand( ) {
		super("campaign", "generic command for campaigns");
		
		registerSubcommand(new CampaignInfoCommand());
		registerSubcommand(new CampaignSelectCommand());
	}
}
