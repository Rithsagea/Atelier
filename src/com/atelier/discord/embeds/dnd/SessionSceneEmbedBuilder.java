package com.atelier.discord.embeds.dnd;

import com.atelier.discord.AtelierEmbedBuilder;
import com.atelier.dnd.campaign.Scene;

public class SessionSceneEmbedBuilder extends AtelierEmbedBuilder {
  public SessionSceneEmbedBuilder(Scene scene) {
    if(scene == null) {
      setTitle("Error");
      setDescription("Missing scene!");
      return;
    }
    setTitle(getMessage("title").addScene(scene).get());
    setDescription(getMessage("description")
      .addScene(scene)
      .get());
  }
}
