package com.atelier.discord.commands.dnd;

import java.util.UUID;
import java.util.stream.Collectors;

import com.atelier.database.AtelierDB;
import com.atelier.discord.AtelierUser;
import com.atelier.discord.commands.BaseInteraction.GroupCommand;
import com.atelier.dnd.campaign.Campaign;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SessionCommand extends GroupCommand {

  private class SessionStart extends BaseSubcommand {

    private String campaign = getProperty("campaign.name");
    private String campaingDescription = getProperty("campaign.description");

    @Override
    public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
      Campaign campaign = AtelierDB.getInstance().getCampaign(UUID.fromString(event.getOption(this.campaign).getAsString()));
      event.reply("Selected Campaign: " + campaign).setEphemeral(true).queue();
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
			data.addOption(OptionType.STRING, campaign, campaingDescription, true, true);
		}
  }

  @Override
  public CommandData getData() {
    return super.getData().setDefaultEnabled(false);
  }

  public SessionCommand() {
    registerSubcommand(new SessionStart());
  }
}
