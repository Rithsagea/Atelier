package com.atelier.dnd.action;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.rithsagea.util.event.Event;

public class LoadActionsEvent implements Event {
  
  private Set<Action> actions = new HashSet<>();
  
  public void addAction(Action action) {
    actions.add(action);
  }

  public Set<Action> getActions() {
    return Collections.unmodifiableSet(actions);
  }
}
