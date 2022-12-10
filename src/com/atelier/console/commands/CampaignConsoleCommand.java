package com.atelier.console.commands;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleGroupCommand;
import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.ConsoleCache;
import com.atelier.database.AtelierDB;
import com.atelier.dnd.campaign.Campaign;
import com.atelier.dnd.character.AtelierCharacter;

public class CampaignConsoleCommand extends BaseConsoleGroupCommand {

	private class CampaignList extends BaseConsoleSubcommand {
		public CampaignList() {
			super("list");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			logger.info(getMessage("info").get());
			AtelierDB.getInstance().listCampaigns()
				.sorted(Comparator.comparing(Campaign::getName)
					.thenComparing(Campaign::getId))
				.forEach(c -> logger.info(c.toString()));
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

			cache.selectCampaign(campaign);
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

	private class CampaignAddCharacter extends BaseConsoleSubcommand {
		public CampaignAddCharacter() {
			super("addcharacter");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			Campaign selectedCampaign = cache.selectedCampaign();
			AtelierCharacter character = AtelierDB.getInstance().getCharacter(UUID.fromString(args[2]));

			selectedCampaign.addCharacter(character);
			logger.info(getMessage("info")
				.addCharacter(character)
				.addCampaign(selectedCampaign)
				.get());
		}
	}

	private class CampaignRemoveCharacter extends BaseConsoleSubcommand {
		public CampaignRemoveCharacter() {
			super("removecharacter");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			Campaign selectedCampaign = cache.selectedCampaign();
			AtelierCharacter character = AtelierDB.getInstance().getCharacter(UUID.fromString(args[2]));

			if(selectedCampaign.removeCharacter(character))
				logger.info(getMessage("info")
						.addCharacter(character)
						.addCampaign(selectedCampaign)
						.get());
			else
				logger.info(getMessage("missing")
						.addCharacter(character)
						.addCampaign(selectedCampaign)
						.get());
		}
	}

	private class CampaignListCharacter extends BaseConsoleSubcommand {
		public CampaignListCharacter() {
			super("listcharacter");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			Campaign selectedCampaign = cache.selectedCampaign();

			logger.info(getMessage("info")
				.addCampaign(selectedCampaign).get());
			selectedCampaign.getCharacters()
				.sorted(Comparator.comparing(AtelierCharacter::getName)
					.thenComparing(AtelierCharacter::getId))
				.forEach(c -> logger.info(c.toString()));
		}
	}

	private ConsoleCache cache;

	public CampaignConsoleCommand(ConsoleCache cache) {
		super("campaign");
		this.cache = cache;

		registerSubcommand(new CampaignList());
		registerSubcommand(new CampaignSelect());
		registerSubcommand(new CampaignNew());
		registerSubcommand(new CampaignAddCharacter());
		registerSubcommand(new CampaignRemoveCharacter());
		registerSubcommand(new CampaignListCharacter());
	}

	@Override
	public void executeDefault(String[] args, Logger logger) {
		Campaign selectedCampaign = cache.selectedCampaign();

		logger.info("Name: " + selectedCampaign.getName());
		logger.info("Id: " + selectedCampaign.getId());
		logger.info("Characters: " + selectedCampaign.getCharacters().map(AtelierCharacter::getName).collect(Collectors.toList()));
	}
}
