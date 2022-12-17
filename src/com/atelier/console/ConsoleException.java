package com.atelier.console;

import com.atelier.database.AtelierObject;

public class ConsoleException extends RuntimeException implements AtelierObject {

  public String getLogMessage() {
    return getMessage("log").get();
  }

  public static class UnselectedCharacterException extends ConsoleException {}
  public static class UnselectedUserException extends ConsoleException {}
  public static class UnselectedCampaignException extends ConsoleException {}
  public static class UnselectedSceneException extends ConsoleException {}
  public static class UnselectedSessionException extends ConsoleException {}

  public static class MissingArgumentException extends ConsoleException {
    
    private String argumentName;
    public MissingArgumentException(String argumentName) {
      this.argumentName = argumentName;
    }

    public String getLogMessage() {
      return getMessage("log").add("argumentName", argumentName).get();
    }
  }

  public static class UnknownArgumentException extends ConsoleException {
    
    private String argument;
    public UnknownArgumentException(String argument) {
      this.argument = argument;
    }

    public String getLogMessage() {
      return getMessage("log").add("argument", argument).get();
    }
  }
}
