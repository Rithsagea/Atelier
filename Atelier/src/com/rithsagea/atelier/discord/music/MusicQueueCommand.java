package com.rithsagea.atelier.discord.music;

import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.User;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

public class MusicQueueCommand extends MusicSubCommand {

	public MusicQueueCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}
	
	@Override
	public String getLabel() {
		return "queue";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("q");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(User user, AtelierAudioHandler audioHandler,  List<String> args, MessageReceivedEvent event) {
		List<AudioTrack> tracks = audioHandler.getQueue();
		
		int pages = tracks.size() / 10 + 1;
		int page = 0;
		int pos = page * 10;
		
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("Page **%d** of **%d**	\n\n", page + 1, pages));
		for(int x = 0; x < 10; x++) {
			if(pos > tracks.size()) break; // no more tracks left
			
			AudioTrack track = tracks.get(pos);
			builder.append(String.format("[%d] **%s**\n", pos + 1, track.getInfo().title));
			pos++;
		}
		builder.append(String.format("\nTotal of **%d** songs in queue", tracks.size()));
		
		event.getChannel().sendMessage(builder.toString())
			.setActionRows(
					ActionRow.of(
						SelectMenu.create("menu")
							.addOption("test1", "test1")
							.addOption("test2", "test2")
							.addOption("test3", "test3")
							.setMaxValues(5)
							.build()),
					ActionRow.of(
						Button.primary("prev", "Previous"),
						Button.primary("next", "Next"))
			).queue();
		//TODO create pagination here, use action buttons
		// possibly add external persistent holder for
		// message action information
	}

}
