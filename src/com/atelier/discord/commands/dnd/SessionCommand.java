package com.atelier.discord.commands.dnd;

import java.util.UUID;
import java.util.stream.Collectors;

import com.atelier.database.AtelierDB;
import com.atelier.discord.AtelierUser;
import com.atelier.discord.commands.BaseInteraction.GroupCommand;
import com.atelier.discord.embeds.dnd.SessionNewEmbedBuilder;
import com.atelier.discord.embeds.dnd.SessionSceneEmbedBuilder;
import com.atelier.dnd.SessionManager;
import com.atelier.dnd.campaign.Campaign;
import com.atelier.dnd.campaign.Scene;
import com.atelier.dnd.campaign.Session;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SessionCommand extends GroupCommand {

  private class SessionStart extends BaseSubcommand {

    private String campaign = getProperty("campaign.name");
    private String campaignDescription = getProperty("campaign.description");

    @Override
    public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
      Campaign campaign = AtelierDB.getInstance().getCampaign(UUID.fromString(event.getOption(this.campaign).getAsString()));
      Session session = SessionManager.getInstance().createSession(campaign, event.getMessageChannel());
      event.replyEmbeds(new SessionNewEmbedBuilder(session).build()).queue();
    }
    
    @Override
		public void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event) {
      if(event.getFocusedOption().getName().equals(campaign)) {
        event.replyChoices(AtelierDB.getInstance().listCampaigns()
          .filter((Campaign c) -> c.toString().startsWith(event.getFocusedOption().getValue()))
          .map((Campaign c) -> new Command.Choice(c.getName(), c.getId().toString()))
          .collect(Collectors.toList())).queue();
      }
    }

    @Override
		public void addOptions(SubcommandData data) {
			data.addOption(OptionType.STRING, campaign, campaignDescription, true, true);
		}
  }

  private class SessionScene extends BaseSubcommand {
    
    private String scene = getProperty("scene.name");
    private String sceneDescription = getProperty("scene.description");
    
    @Override
    public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
      Session session = SessionManager.getInstance().getSession(event.getChannel());

      if(event.getOption(scene) != null)
        session.setScene(AtelierDB.getInstance().getScene(UUID.fromString(event.getOption(scene).getAsString())));

      event.replyEmbeds(new SessionSceneEmbedBuilder(session.getScene()).build()).queue();
    }

    @Override
    public void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event) {
      if(event.getFocusedOption().getName().equals(scene)) {
        Session session = SessionManager.getInstance().getSession(event.getMessageChannel());
        
        if(session == null) {
          event.replyChoices().queue();
        } else {
          event.replyChoices(session.getCampaign().listScenes()
            .filter((Scene s) -> s.toString().startsWith(event.getFocusedOption().getValue()))
            .map((Scene s) -> new Command.Choice(s.getName(), s.getId().toString()))
            .collect(Collectors.toList())).queue();
        }
      }
    }

    @Override
		public void addOptions(SubcommandData data) {
			data.addOption(OptionType.STRING, scene, sceneDescription, false, true);
		}
  }

  @Override
  public CommandData getData() {
    return super.getData().setDefaultEnabled(false);
  }

  public SessionCommand() {
    registerSubcommand(new SessionStart());
    registerSubcommand(new SessionScene());
  }
}
