package com.atelier.discord.embeds.dnd;

import com.atelier.discord.AtelierEmbedBuilder;
import com.atelier.dnd.campaign.Session;

public class SessionNewEmbedBuilder extends AtelierEmbedBuilder {
  public SessionNewEmbedBuilder(Session session) {
    setTitle(getMessage("title").addCampaign(session.getCampaign()).get());
    setDescription(getMessage("description")
      .addCampaign(session.getCampaign())
      .addChannel(session.getChannel())
      .get());
  }
}
