package com.atelier.discord.commands.music;

import com.atelier.discord.AtelierUser;
import com.atelier.util.AtelierLanguageManager;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class MusicPlayCommand extends MusicSubCommand {

	private final String optionUrlName = AtelierLanguageManager.getInstance().get(this, "url.name");
	private final String optionUrlDescription = AtelierLanguageManager.getInstance().get(this, "url.description");
	
	@Override
	public void execute(AtelierAudioHandler audioHandler, AtelierUser user, SlashCommandInteractionEvent event) {
		String url = event.getOption("url").getAsString();
		event.reply("Loading url: " + url).queue();
		
		if (!event.getGuild().getAudioManager().isConnected())
			if (audioHandler.join(event) == null) return;
		
		audioHandler.loadTrack(url);
	}

	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.STRING, optionUrlName, optionUrlDescription, true, false);
	}
}
