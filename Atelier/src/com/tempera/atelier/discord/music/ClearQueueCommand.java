package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class ClearQueueCommand extends MusicSubCommand{

	public ClearQueueCommand(AtelierAudioManager audioManager, String name, String description) {
		super(audioManager, "clear", "Empties the entire queue");
	}


	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		if (event.getOption("areyousure").getAsBoolean()) {
			for (int i = 0; i < audioHandler.getQueue().size(); i++)
				audioHandler.nextTrack();
			event.reply("Clearing queue!");
		}
	}
	
	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.BOOLEAN, "areyousure", "Are you sure?", true, false);
	}
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
