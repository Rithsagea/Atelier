package com.tempera.atelier.discord.commands.music;

import com.tempera.atelier.discord.commands.GroupCommand;

public class MusicCommand extends GroupCommand {

	public MusicCommand() {
		super("music", "For managing and playing music in vc");

		registerSubcommand(new MusicPlayCommand());
		registerSubcommand(new MusicQueueCommand());
		registerSubcommand(new MusicJoinCommand());
		registerSubcommand(new MusicLoopCommand());
		registerSubcommand(new MusicPauseCommand()); //TODO rework
		registerSubcommand(new MusicNowPlayingCommand());
		registerSubcommand(new MusicSkipCommand());
		registerSubcommand(new MusicDisconnectCommand());
		registerSubcommand(new MusicSansCommand());
		registerSubcommand(new MusicClearQueueCommand());
	}

}
