package com.rithsagea.atelier.discord.music;

import java.util.List;

import com.rithsagea.atelier.AtelierBot;
import com.rithsagea.atelier.discord.AtelierSubCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicCommand extends AtelierSubCommand {

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
		return null;
	}

	@Override
	public void executeDefault(List<String> args, MessageReceivedEvent event) {
		
	}

}
