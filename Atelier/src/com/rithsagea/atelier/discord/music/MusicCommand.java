package com.rithsagea.atelier.discord.music;

import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.AtelierGroupCommand;
import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicCommand extends AtelierGroupCommand {

	private AtelierAudioManager audioManager;
	
	public MusicCommand(AtelierBot bot) {
		audioManager = bot.getAudioManager();
		
		registerCommand(new MusicJoinCommand(audioManager));
		registerCommand(new MusicPlayCommand(audioManager));
	}
	
	@Override
	public String getLabel() {
		return "music";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("audio");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}
	
	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		
	}

}
