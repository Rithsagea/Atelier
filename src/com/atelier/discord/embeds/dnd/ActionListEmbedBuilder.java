package com.atelier.discord.embeds.dnd;

import com.atelier.discord.AtelierEmbedBuilder;
import com.atelier.dnd.character.AtelierCharacter;

public class ActionListEmbedBuilder extends AtelierEmbedBuilder {
  public ActionListEmbedBuilder(AtelierCharacter character) {
    StringBuilder content = new StringBuilder();
    setTitle(getMessage("title").addCharacter(character).get());

    character.getActions().forEach(a -> {
      content.append(getMessage("action")
        .add("actionName", a.getName())
        .get());
      content.append("\n");
    });
    
    setDescription(content);
  }
}
