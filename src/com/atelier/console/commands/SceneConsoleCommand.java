package com.atelier.console.commands;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleGroupCommand;
import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.ConsoleCache;

public class SceneConsoleCommand extends BaseConsoleGroupCommand {
  
  private class SceneList extends BaseConsoleSubcommand {
    public SceneList() {
      super("list");
    }

    @Override
    public void execute(String[] args, Logger logger) {
      
    }
  }
  
  private ConsoleCache cache;

  public SceneConsoleCommand(ConsoleCache cache) {
    super("scene");
    this.cache = cache;

    registerSubcommand(new SceneList());
  }

  @Override
  public void executeDefault(String[] args, Logger logger) {

  }
}
