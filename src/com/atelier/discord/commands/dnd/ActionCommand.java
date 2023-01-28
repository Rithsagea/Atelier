package com.atelier.discord.commands.dnd;

import com.atelier.discord.AtelierUser;
import com.atelier.discord.commands.BaseInteraction.GroupCommand;
import com.atelier.discord.embeds.dnd.ActionListEmbedBuilder;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class ActionCommand extends GroupCommand {
  private class ActionList extends BaseSubcommand {
    @Override
    public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
      event.replyEmbeds(new ActionListEmbedBuilder(user.getSelectedCharacter())
        .build()).setEphemeral(true).queue();
    }
  }

  private class ActionPerform extends BaseSubcommand {

    private String action = getProperty("action.name");
		private String actionDescription = getProperty("action.description");

    @Override
    public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
      
    }

    @Override
    public void addOptions(SubcommandData data) {
      data.addOption(OptionType.STRING, action, actionDescription, true, true);
    }

    @Override
    public void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event) {
      // AtelierCharacter character = user.getSelectedCharacter();
      // event.replyChoices(character.getActions()
      //   .map(a -> new Command.Choice(actionDescription, action)))
    }
  }

  public ActionCommand() {
    registerSubcommand(new ActionList());
    registerSubcommand(new ActionPerform());
  }
}
