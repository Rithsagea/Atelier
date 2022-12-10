package com.atelier.console.commands;

import java.util.UUID;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleGroupCommand;
import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.ConsoleCache;
import com.atelier.console.ConsoleException.MissingArgumentException;
import com.atelier.console.ConsoleException.UnknownArgumentException;
import com.atelier.database.AtelierDB;
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
      AtelierDB.getInstance().listScenes()
        // campaign.getScenes()
        .forEach(s -> logger.info(s.toString()));
    }
  }
  
  private class SceneSelect extends BaseConsoleSubcommand {
    public SceneSelect() {
      super("select");
    }

    @Override
    public void execute(String[] args, Logger logger) {
      Scene scene = AtelierDB.getInstance().getScene(UUID.fromString(args[2]));
      logger.info(getMessage("info").addScene(scene).get());

      cache.selectScene(scene);
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

  private class SceneEdit extends BaseConsoleSubcommand {
    public SceneEdit() {
      super("edit");
    }

    @Override
    public void execute(String[] args, Logger logger) {
      if(args.length <= 2)
        throw new MissingArgumentException("field");
      switch(args[1]) {
        default:
          throw new UnknownArgumentException(args[1]);
      }
    }
  }

  private ConsoleCache cache;

  public SceneConsoleCommand(ConsoleCache cache) {
    super("scene");
    this.cache = cache;

    registerSubcommand(new SceneList());
    registerSubcommand(new SceneSelect());
    registerSubcommand(new SceneNew());
    registerSubcommand(new SceneEdit());
  }

  @Override
  public void executeDefault(String[] args, Logger logger) {
    Scene scene = cache.selectedScene();

    logger.info("Name: " + scene.getName());
    logger.info("Id: " + scene.getId());
  }
}
