package com.atelier.dnd.commands.campaign;

import com.atelier.discord.commands.BaseInteraction.GroupCommand;

public class CampaignCommand extends GroupCommand {
	public CampaignCommand( ) {
		registerSubcommand(new CampaignInfoCommand());
		registerSubcommand(new CampaignSelectCommand());
	}
}
