package com.tempera.atelier.discord.music;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.aaagarbage.MenuManager;
import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class QueueCommand extends MusicSubCommand {

	private MenuManager menuManager;

	public QueueCommand(AtelierBot bot) {
		super(bot.getAudioManager(), "queue", "Displays the current queue of tracks");
		menuManager = bot.getMenuManager();
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		int page = 1;
		if (event.getOption("page") != null) {
			page = (int) event.getOption("page").getAsLong() <= 1 ? 1 
				: Math.min((int) event.getOption("page").getAsLong(),
					(int) Math.ceil(audioHandler.getQueue()
						.size() / 10d));
		}
			menuManager.addMenu(event.getChannel(),
				new QueueMenu(audioHandler, menuManager, page));
	}

	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.INTEGER, "page", "The page number", false, false);
	}
	
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
