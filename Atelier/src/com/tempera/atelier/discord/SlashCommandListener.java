package com.tempera.atelier.discord;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.Config;
import com.tempera.atelier.discord.commands.SlashCommandRegistry;
import com.tempera.atelier.discord.commands.StopCommand;
import com.tempera.atelier.discord.commands.WaifuCommand;
import com.tempera.atelier.discord.commands.character.CharacterCommand;
import com.tempera.atelier.dnd.types.AtelierDB;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {
	private SlashCommandRegistry reg = new SlashCommandRegistry();
	
	private AtelierBot bot;
	private AtelierDB db = AtelierDB.getInstance();
	private Config config = Config.getInstance();
	private SlashMenuManager menus = SlashMenuManager.getInstance();
	
	public SlashCommandListener(AtelierBot bot) {
		this.bot = bot;
		
		registerCommands();
	}
	
	private void registerCommands() {
		reg.registerCommand(new WaifuCommand());
		reg.registerCommand(new StopCommand(bot));
		
		reg.registerCommand(new CharacterCommand());
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		Guild guild = event.getJDA().getGuildById(config.getTestingGuildId());
		reg.getCommands().stream()
			.map(cmd->cmd.getData())
			.map(guild::upsertCommand)
			.forEach(a -> a.queue());
		
		//TODO global commands here
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if(event.getUser().isBot()) return;
		
		net.dv8tion.jda.api.entities.User author = event.getUser();
		User user = db.getUser(author.getIdLong());
		user.setName(author.getName());
		
		reg.getCommand(event.getName()).execute(user, event);
	}
	
	@Override
	public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
		if(event.getUser().isBot()) return;
		
		net.dv8tion.jda.api.entities.User author = event.getUser();
		User user = db.getUser(author.getIdLong());
		user.setName(author.getName());
		
		reg.getCommand(event.getName()).complete(user, event);
	}

	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		SlashMenu menu = menus.getMenu(event.getMessageIdLong());
		if(menu != null) menu.onButtonInteract(event);
	}
	
	@Override
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
		SlashMenu menu = menus.getMenu(event.getMessageIdLong());
		if(menu != null) menu.onSelectInteract(event);
	}
}
