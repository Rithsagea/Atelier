package com.atelier.dnd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.atelier.dnd.campaign.Campaign;
import com.atelier.dnd.campaign.Session;

import net.dv8tion.jda.api.entities.MessageChannel;

public class SessionManager {
  
  private static final SessionManager INSTANCE = new SessionManager();
  public static SessionManager getInstance() {
    return INSTANCE;
  }
  
  private Map<Long, Session> sessions = new HashMap<>();

  private SessionManager() {}

  public Session createSession(Campaign campaign, MessageChannel channel) {
    Session session = new Session(campaign, channel);
    sessions.put(session.getChannel().getIdLong(), session);
    return session;
  }

  public Map<Long, Session> getSessions() {
    return Collections.unmodifiableMap(sessions);
  }

  public Session getSession(MessageChannel channel) {
    return sessions.get(channel.getIdLong());
  }
}
