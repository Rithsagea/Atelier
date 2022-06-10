package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Campaign;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditUserCommand extends GroupCommand {

	private AtelierDB db;
	
	public EditUserCommand(AtelierBot bot) {
		super("user", DataUtil.list("u"), PermissionLevel.USER);
		
		db = bot.getDatabase();
	}

	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		long id = -1;
		
		try {
			id = Long.parseLong(args.get(1));
		} catch (NumberFormatException e) { }
		try {
			id = Long.parseLong(args.get(1).substring(2, 20));
		} catch (NumberFormatException e) { }
		
		User target = db.getUser(id);
		Sheet sheet = target == null ? null : db.getSheet(target.getSheetId());
		Campaign campaign = target == null ? null : db.getCampaign(target.getCampaignId());
		event.getChannel().sendMessage(
				String.format("Viewing: %s\nLevel: %s\nSheet: %s\nCampaign: %s", target, target.getLevel(), sheet, campaign)).queue();
	}
	
}
