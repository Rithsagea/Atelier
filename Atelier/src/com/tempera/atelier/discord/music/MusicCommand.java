package com.tempera.atelier.discord.music;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.commands.AtelierCommand;
import com.tempera.atelier.discord.commands.CommandRegistry;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.User;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicCommand extends GroupCommand {

	private AtelierAudioManager audioManager;
	
	public MusicCommand(AtelierBot bot) {
		audioManager = bot.getAudioManager();
		CommandRegistry registry = this.getCommandRegistry();
		registry.registerCommand(new PlayCommand(audioManager));
		registry.registerCommand(new QueueCommand(bot));
		registry.registerCommand(new JoinCommand(audioManager));
		registry.registerCommand(new PlayingCommand(audioManager));
		registry.registerCommand(new SansCommand(audioManager));
	}
	
	@Override
	public String getLabel() {
		return "music";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("m", "audio");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}
	
	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		
		if(args.size() > 1) {
			AtelierCommand cmd = this.getCommandRegistry().getCommand(args.get(1)); // 2nd argument should be subcommand
			if(cmd != null) {
				cmd.execute(user, args.subList(1, args.size()), event); //don't include previous argument
				return;
			}
		}
		
		executeDefault(user, args, event);
	}
	
	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.ORANGE);
		eb.setTitle("List of music commands:");
		eb.addField("join", "Joins the user's current voice channel", true);
		eb.addField("play [URL]", "Adds a song or playlist from a URL if possible", true);
		eb.addField("playing / np", "Displays currently playing song", true);
		eb.addField("queue [PAGE NUM]", "Displays queued up songs. Optional page number as a second argument.", true);
		eb.addField("sans", "bad time", true);
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}
}
