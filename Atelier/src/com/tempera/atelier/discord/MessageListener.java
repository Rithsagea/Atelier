package com.tempera.atelier.discord;

import com.tempera.atelier.dnd.types.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

	private AtelierDB db = AtelierDB.getInstance();
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		net.dv8tion.jda.api.entities.User author = event.getAuthor();

		User user = db.getUser(author.getIdLong());
		user.setName(author.getName());
	}
}
