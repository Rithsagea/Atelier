package com.atelier.console;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import com.atelier.console.ConsoleException.UnselectedCampaignException;
import com.atelier.console.ConsoleException.UnselectedCharacterException;
import com.atelier.console.ConsoleException.UnselectedSceneException;
import com.atelier.console.ConsoleException.UnselectedSessionException;
import com.atelier.console.ConsoleException.UnselectedUserException;
import com.atelier.database.AtelierDB;
import com.atelier.discord.AtelierUser;
import com.atelier.dnd.campaign.Campaign;
import com.atelier.dnd.campaign.Scene;
import com.atelier.dnd.campaign.Session;
import com.atelier.dnd.character.AtelierCharacter;
import com.rithsagea.util.data.DataUtil;

public class ConsoleCache {

  private static ConsoleCache INSTANCE;
  public static ConsoleCache init(String cachePath) {
    return (INSTANCE = new ConsoleCache(cachePath));
  }

  public static ConsoleCache getInstance() {
    return INSTANCE;
  }

  private AtelierCharacter selectedCharacter;
  private AtelierUser selectedUser;
  private Campaign selectedCampaign;
  private Scene selectedScene;
  private transient Session selectedSession;

  private File cacheFile;
  private Properties properties;

  private ConsoleCache(String cachePath) {
    cacheFile = new File(cachePath);
    properties = new Properties();

    if(!cacheFile.exists()) {
      try {
        cacheFile.createNewFile();
      } catch(IOException e) {
        e.printStackTrace();
      }
    }

    load();

  }

  public AtelierCharacter selectedCharacter() {
    if(selectedCharacter == null)
      throw new UnselectedCharacterException();
    return selectedCharacter;
  }

  public AtelierUser selectedUser() {
    if(selectedUser == null)
      throw new UnselectedUserException();
    return selectedUser;
  }

  public Campaign selectedCampaign() {
    if(selectedCampaign == null)
      throw new UnselectedCampaignException();
    return selectedCampaign;
  }

  public Scene selectedScene() {
    if(selectedScene == null)
      throw new UnselectedSceneException();
    return selectedScene;
  }

  public Session selectedSession() {
    if(selectedSession == null)
      throw new UnselectedSessionException();
    return selectedSession;
  }


  public void selectCharacter(AtelierCharacter character) {
    selectedCharacter = character;
  }

  public void selectUser(AtelierUser user) {
    selectedUser = user;
  }

  public void selectCampaign(Campaign campaign) {
    selectedCampaign = campaign;
  }

  public void selectScene(Scene scene) {
    selectedScene = scene;
  }

  public void selectSession(Session session) {
    selectedSession = session;
  }


  public void load() {
    AtelierDB db = AtelierDB.getInstance();

    try {
      properties.load(new FileReader(cacheFile));
    } catch(Exception e) {
      e.printStackTrace();
    }

    selectedUser = db.getUser(DataUtil.stringToLong(properties.getProperty("selectedUser")));
    selectedCharacter = db.getCharacter(DataUtil.stringToUuid(properties.getProperty("selectedCharacter")));
    selectedCampaign = db.getCampaign(DataUtil.stringToUuid(properties.getProperty("selectedCampaign")));
    selectedScene = db.getScene(DataUtil.stringToUuid(properties.getProperty("selectedScene")));
  }

  public void save() {
    if(selectedCharacter != null)
      properties.setProperty("selectedCharacter", selectedCharacter.getId().toString());
    if(selectedUser != null)
      properties.setProperty("selectedUser", Long.toString(selectedUser.getId()));
    if(selectedCampaign != null)
      properties.setProperty("selectedCampaign", selectedCampaign.getId().toString());
    if(selectedScene != null)
      properties.setProperty("selectedScene", selectedScene.getId().toString());

    try {
      properties.store(new FileWriter(cacheFile), "");
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

}
