package com.atelier.dnd.action;

import com.atelier.database.AtelierObject;

public class BaseAction implements AtelierObject, Action {
  
  public String getName() {
    return getProperty("name");
  }
}
