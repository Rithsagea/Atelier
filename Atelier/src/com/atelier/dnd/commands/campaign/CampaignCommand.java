package com.atelier.dnd.commands.campaign;

import com.atelier.discord.commands.GroupCommand;

public class CampaignCommand extends GroupCommand {
	public CampaignCommand( ) {
		super("campaign", "generic command for campaigns");
		
		registerSubcommand(new CampaignInfoCommand());
		registerSubcommand(new CampaignSelectCommand());
	}
}
