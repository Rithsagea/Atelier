package com.tempera.atelier.discord.commands.music;

import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class MusicQueueCommand extends MusicSubCommand {

	private MenuManager menuManager = MenuManager.getInstance();

	public MusicQueueCommand() {
		super("queue", "Displays the current queue of tracks");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		int page = 1;
		if (event.getOption("page") != null) {
			page = (int) event.getOption("page").getAsLong() <= 1 ? 1 
				: Math.min((int) event.getOption("page").getAsLong(),
					(int) Math.ceil(audioHandler.getQueue().size() / 10d));
		}
			menuManager.addMenu(new MusicQueueMenu(audioHandler, page), false, event);
	}

	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.INTEGER, "page", "The page number", false, false);
	}
	
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
