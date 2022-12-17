package com.atelier.dnd;

import com.atelier.dnd.campaign.Campaign;
import com.atelier.dnd.campaign.Session;

import net.dv8tion.jda.api.entities.MessageChannel;

public class SessionManager {
  
  private static final SessionManager INSTANCE = new SessionManager();
  public static SessionManager getInstance() {
    return INSTANCE;
  }
  
  private SessionManager() {}

  public Session createSession(Campaign campaign, MessageChannel channel) {
    return new Session(campaign, channel);
  }
}
