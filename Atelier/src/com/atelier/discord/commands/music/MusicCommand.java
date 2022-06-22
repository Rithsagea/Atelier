package com.atelier.discord.commands.music;

import com.atelier.discord.commands.BaseInteraction.GroupCommand;

public class MusicCommand extends GroupCommand {

	public MusicCommand() {
		registerSubcommand(new MusicPlayCommand());
		registerSubcommand(new MusicQueueCommand());
		registerSubcommand(new MusicJoinCommand());
		registerSubcommand(new MusicLoopCommand());
		registerSubcommand(new MusicPauseCommand()); //TODO rework
		registerSubcommand(new MusicNowPlayingCommand());
		registerSubcommand(new MusicSkipCommand());
		registerSubcommand(new MusicDisconnectCommand());
		registerSubcommand(new MusicSansCommand());
		registerSubcommand(new MusicClearCommand());
	}

}
