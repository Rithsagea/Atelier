package com.tempera.atelier.discord.music;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.commands.SlashGroupCommand;

public class MusicCommand extends SlashGroupCommand {

	public MusicCommand(AtelierBot bot) {
		super("music", "For managing and playing music in vc");

		AtelierAudioManager audioManager = bot.getAudioManager();
	
		registerSubcommand(new PlayCommand(audioManager));
		registerSubcommand(new QueueCommand(bot));
		registerSubcommand(new JoinCommand(audioManager));
		registerSubcommand(new LoopCommand(audioManager));
		registerSubcommand(new TogglePauseCommand(audioManager));
		registerSubcommand(new NowPlayingCommand(audioManager));
		registerSubcommand(new SkipCommand(audioManager));
		registerSubcommand(new DisconnectCommand(audioManager));
		registerSubcommand(new SansCommand(audioManager));
		registerSubcommand(new SansListCommand(audioManager));
		registerSubcommand(new ClearQueueCommand(audioManager));
	}

}
