package com.atelier.console.commands;

import java.util.Comparator;
import java.util.UUID;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleGroupCommand;
import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.database.AtelierDB;
import com.atelier.dnd.campaign.Campaign;

public class CampaignConsoleCommand extends BaseConsoleGroupCommand {

	private class CampaignList extends BaseConsoleSubcommand {
		public CampaignList() {
			super("list");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			logger.info(getMessage("info").get());
			AtelierDB.getInstance().listCampaigns()
				.stream()
				.sorted(new Comparator<Campaign>() {
					@Override
					public int compare(Campaign u1, Campaign u2) {
						if(!u1.getName().equals(u2.getName()))
							return u1.getName().compareTo(u2.getName());
						return u1.getId().compareTo(u2.getId());
					}
				}).forEach(c -> logger.info(c.toString()));
		}
	}
	

	private class CampaignSelect extends BaseConsoleSubcommand {
		public CampaignSelect() {
			super("select");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			Campaign campaign = AtelierDB.getInstance().getCampaign(UUID.fromString(args[2]));
			logger.info(getMessage("info").addCampaign(campaign).get());

			selectedCampaign = campaign;
		}
	}

	private class CampaignNew extends BaseConsoleSubcommand {
		public CampaignNew() {
			super("new");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			Campaign campaign = new Campaign();
			AtelierDB.getInstance().addCampaign(campaign);
			logger.info(getMessage("info").addCampaign(campaign).get());
		}
	}

	private Campaign selectedCampaign;

	public CampaignConsoleCommand() {
		super("campaign");

		registerSubcommand(new CampaignList());
		registerSubcommand(new CampaignSelect());
		registerSubcommand(new CampaignNew());
	}

	@Override
	public void executeDefault(String[] args, Logger logger) {
		if(selectedCampaign == null) {
			logger.info(getMessage("missing").get());
			return;
		}

		logger.info("Name: " + selectedCampaign.getName());
		logger.info("Id: " + selectedCampaign.getId());
	}
}
