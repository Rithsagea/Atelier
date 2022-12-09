package com.atelier.console.commands;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleGroupCommand;
import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.ConsoleCache;
import com.atelier.dnd.campaign.Campaign;
import com.atelier.dnd.campaign.Scene;

public class SceneConsoleCommand extends BaseConsoleGroupCommand {
  
  private class SceneList extends BaseConsoleSubcommand {
    public SceneList() {
      super("list");
    }

    @Override
    public void execute(String[] args, Logger logger) {
      Campaign campaign = cache.selectedCampaign();

      logger.info(getMessage("info")
        .addCampaign(campaign).get()); //TODO sorting goes here
      campaign.getScenes()
        .forEach(s -> logger.info(s.toString()));
    }
  }
  
  private class SceneNew extends BaseConsoleSubcommand {
    public SceneNew() {
      super("new");
    }

    @Override
    public void execute(String[] args, Logger logger) {
      Campaign campaign = cache.selectedCampaign();
      Scene scene = new Scene();
      campaign.addScene(scene);

      logger.info(getMessage("info")
        .addScene(scene)
        .addCampaign(campaign)
        .get());
    }
  }

  private ConsoleCache cache;

  public SceneConsoleCommand(ConsoleCache cache) {
    super("scene");
    this.cache = cache;

    registerSubcommand(new SceneList());
    registerSubcommand(new SceneNew());
  }

  @Override
  public void executeDefault(String[] args, Logger logger) {

  }
}
