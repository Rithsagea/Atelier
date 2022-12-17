package com.atelier.console.commands;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleGroupCommand;
import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.ConsoleCache;
import com.atelier.dnd.SessionManager;
import com.atelier.dnd.campaign.Session;

public class SessionConsoleCommand extends BaseConsoleGroupCommand {
  
  private class SessionList extends BaseConsoleSubcommand {
    public SessionList() {
      super("list");
    }

    @Override
    public void execute(String[] args, Logger logger) {
      logger.info(getMessage("info").get());
      SessionManager.getInstance().getSessions().entrySet().forEach(
        e -> logger.info("{}: {}", e.getValue().getChannel().getAsMention(), e.getValue().getCampaign()));
    }
  }

  private class SessionSelect extends BaseConsoleSubcommand {
    public SessionSelect() {
      super("select");
    }

    @Override
    public void execute(String[] args, Logger logger) {
      Session session = SessionManager.getInstance().getSessions().get(Long.parseLong(args[2]));
      logger.info(getMessage("info")
        .addCampaign(session.getCampaign())
        .addChannel(session.getChannel()).get());
      cache.selectSession(session);
    }
  }

  private ConsoleCache cache;

  public SessionConsoleCommand(ConsoleCache cache) {
    super("session");
    this.cache = cache;

    registerSubcommand(new SessionList());
    registerSubcommand(new SessionSelect());
  }

  @Override
	public void executeDefault(String[] args, Logger logger) {
		Session session = cache.selectedSession();
		
		logger.info("Campaign: " + session.getCampaign());
		logger.info("Channel: " + session.getChannel().getAsMention());
	}
}
