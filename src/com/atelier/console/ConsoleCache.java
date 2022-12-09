package com.atelier.console;

import com.atelier.console.ConsoleException.UnselectedCampaignException;
import com.atelier.console.ConsoleException.UnselectedCharacterException;
import com.atelier.console.ConsoleException.UnselectedUserException;
import com.atelier.discord.AtelierUser;
import com.atelier.dnd.campaign.Campaign;
import com.atelier.dnd.character.AtelierCharacter;

public class ConsoleCache {
  private AtelierCharacter selectedCharacter;
  private AtelierUser selectedUser;
  private Campaign selectedCampaign;

  public AtelierCharacter selectedCharacter() {
    if(selectedCharacter == null)
      throw new UnselectedCharacterException();
    return selectedCharacter;
  }

  public void selectCharacter(AtelierCharacter character) {
    selectedCharacter = character;
  }

  public AtelierUser selectedUser() {
    if(selectedUser == null)
      throw new UnselectedUserException();
    return selectedUser;
  }

  public void selectUser(AtelierUser user) {
    selectedUser = user;
  }

  public Campaign selectedCampaign() {
    if(selectedCampaign == null)
      throw new UnselectedCampaignException();
    return selectedCampaign;
  }

  public void selectCampaign(Campaign campaign) {
    selectedCampaign = campaign;
  }
}
