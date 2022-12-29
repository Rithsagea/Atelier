package com.atelier.dnd.campaign;

import net.dv8tion.jda.api.entities.MessageChannel;

public class Session {
  
  private Campaign campaign;
  private Scene scene;
  private MessageChannel channel;

  public Session(Campaign campaign, MessageChannel channel) {
    this.campaign = campaign;
    this.channel = channel;
  }

  public Campaign getCampaign() {
    return campaign;
  }

  public Scene getScene() {
    return scene;
  }

  public MessageChannel getChannel() {
    return channel;
  }

  public void setScene(Scene scene) {
    this.scene = scene;
  }
}
