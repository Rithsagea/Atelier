package com.atelier.discord.commands.music;

import com.atelier.AtelierLanguageManager;
import com.atelier.discord.MenuManager;
import com.atelier.discord.AtelierUser;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class MusicQueueCommand extends MusicSubCommand {

	private MenuManager menuManager = MenuManager.getInstance();
	
	private final String optionPageName = AtelierLanguageManager.getInstance().get(this, "page.name");
	private final String optionPageDescription = AtelierLanguageManager.getInstance().get(this, "page.description");

	@Override
	public void execute(AtelierAudioHandler audioHandler, AtelierUser user, SlashCommandInteractionEvent event) {
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
		data.addOption(OptionType.INTEGER, optionPageName, optionPageDescription, false, false);
	}
}
