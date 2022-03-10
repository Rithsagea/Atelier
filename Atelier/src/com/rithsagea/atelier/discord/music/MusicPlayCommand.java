package com.rithsagea.atelier.discord.music;

import java.util.List;

import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicPlayCommand extends MusicSubCommand {

	public MusicPlayCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}
	
	@Override
	public String getLabel() {
		return "play";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}
	
	@Override
	public void execute(User user, AtelierAudioHandler audioHandler, List<String> args, MessageReceivedEvent event) {
		audioHandler.loadTrack(args.get(1));
	}

}
