package com.atelier.dnd.campaign;

import net.dv8tion.jda.api.entities.MessageChannel;

public class Session {
  
  private Campaign campaign;
  private MessageChannel channel;

  public Session(Campaign campaign, MessageChannel channel) {
    this.campaign = campaign;
    this.channel = channel;
  }

  public Campaign getCampaign() {
    return campaign;
  }

  public MessageChannel getChannel() {
    return channel;
  }
}
