package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.Menu;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class QButton extends Menu{

	private QueueMessageBuilder queueMsg;

	public QButton(Message message, QueueMessageBuilder msg) {
		super(message);
		message.editMessageComponents(ActionRow.of(Button.primary("prev", "Previous"), Button.primary("next", "Next"))).queue();
		this.queueMsg = msg;
	}

	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {
		event.deferEdit().queue();
		if (event.getComponentId().equals("prev")) {
			queueMsg.setPage(queueMsg.getPage() - 1);
			queueMsg.calcQueue();
			queueMsg.send(event.getChannel());
		}
		else if (event.getComponentId().equals("next")) {
			queueMsg.setPage(queueMsg.getPage() + 1);
			queueMsg.calcQueue();
			queueMsg.send(event.getChannel());
		}
	}

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) {
		// TODO Auto-generated method stub

	}
}
