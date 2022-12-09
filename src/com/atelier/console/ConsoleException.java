package com.atelier.console;

import com.atelier.database.AtelierObject;

public class ConsoleException extends RuntimeException implements AtelierObject {

  public String getLogMessage() {
    return getMessage("missing").get();
  }

  public static class UnselectedCharacterException extends ConsoleException {}
  public static class UnselectedUserException extends ConsoleException {}
  public static class UnselectedCampaignException extends ConsoleException {}
}
