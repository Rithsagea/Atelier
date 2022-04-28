package com.tempera.atelier.discord.music;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.commands.AtelierCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.User;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;

public class MusicCommand implements AtelierCommand {

	private AtelierAudioManager audioManager;
	
	public MusicCommand(AtelierBot bot) {
		audioManager = bot.getAudioManager();
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
		Guild guild = event.getGuild();
		AtelierAudioHandler audioHandler = audioManager.getAudioHandler(guild);
		
		switch(args.get(1)) {
			
		case "join":
			AudioManager manager = guild.getAudioManager();
			GuildVoiceState state = event.getMember().getVoiceState();
			if(state == null) return;
			AudioChannel channel = state.getChannel();
			if(channel == null) return;
			manager.setSendingHandler(audioHandler);
			manager.openAudioConnection(channel);
			break;
		
		case "play":
			audioHandler.loadTrack(args.get(2));
			break;
		
		case "queue":{
			EmbedBuilder eb = new EmbedBuilder();
			MessageBuilder bt = new MessageBuilder();
			eb.setColor(Color.CYAN);
			StringBuilder msg = new StringBuilder();
			int queueSize = audioHandler.getQueue().size();
			int pageMax = (int)Math.ceil((double)queueSize / 10);
			int page = 1;
			if (args.size() > 2) {
				page = Math.min(Integer.parseInt(args.get(2)), pageMax);
			}
			
			for (int i = (page-1)*10; i < queueSize && i < page*10; i++) {
				msg.append(String.format("`[%d]` - **%s**\n",
						i + 1, 
						audioHandler.getQueue().get(i).getInfo().title));
			}
		
			if (msg.length() == 0) {
				eb.setTitle("No songs in queue!");
				bt.setActionRows(ActionRow.of(Button.success("asdf", "bad time")));
			}
			else {
				eb.setTitle(queueSize + (queueSize > 1 ?" songs" :" song"));
				eb.addField("Queue:", msg.toString(), true);
				eb.appendDescription(String.format("Page %d of %d", page, pageMax));
				eb.setFooter(String.format("Displaying songs %d to %d out of %d", (page-1)*10+1,
						Math.min(page*10, queueSize), queueSize));
			}
			
			bt.setEmbeds(eb.build());
			event.getChannel().sendMessage(bt.build()).queue();
		}
			break;
			
		case "playing":
			EmbedBuilder eb = new EmbedBuilder();
			eb.addField("Now playing:", audioHandler.getPlayingTrack().getInfo().title, true);
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			break;
			
		case "sans":
			audioHandler.loadTrack("https://www.youtube.com/watch?v=wDgQdr8ZkTw");
			event.getChannel().sendMessage("bad time").queue();
			break;
		}
	}

}
