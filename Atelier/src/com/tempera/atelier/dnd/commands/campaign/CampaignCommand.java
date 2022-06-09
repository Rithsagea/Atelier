package com.tempera.atelier.dnd.commands.campaign;

import java.util.List;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.CommandRegistry;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Campaign;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CampaignCommand extends GroupCommand {

	private AtelierDB db;
	
	public CampaignCommand(AtelierBot bot) {
		super("campaign", DataUtil.list("c"), PermissionLevel.ADMINISTRATOR);
		db = bot.getDatabase();
		
		CommandRegistry reg = getCommandRegistry();
		
		reg.registerCommand(new CampaignSelectCommand(bot));
	}
	
	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		Campaign campaign = db.getCampaign(user.getCampaignId());
		event.getChannel().sendMessage("Campaign: " + campaign).queue();
	}
	
}
