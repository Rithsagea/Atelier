package com.atelier.discord.commands.dnd;

import com.atelier.discord.AtelierUser;
import com.atelier.discord.commands.BaseInteraction.GroupCommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ActionCommand extends GroupCommand {
  private class ActionList extends BaseSubcommand {

    @Override
    public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
      user.getSelectedCharacter().getActions(); //TODO this must be implementd, then interate through yadda yadda
      event.reply("Here are your actions").setEphemeral(true).queue();
    }
    
  }

  public ActionCommand() {
    registerSubcommand(new ActionList());
  }
}
