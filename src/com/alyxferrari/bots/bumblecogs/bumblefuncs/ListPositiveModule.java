package com.alyxferrari.bots.bumblecogs.bumblefuncs;

import java.awt.Color;

import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ListPositiveModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;listpos")) {
			String poss = "";
			for (int i = 0; i < BumbleBot.pos.size(); i++) {
				poss += i == BumbleBot.pos.size()-1 ? BumbleBot.pos.get(i) : BumbleBot.pos.get(i) + "\n";
			}
			mevent.getChannel().sendMessage(BumbleBot.getEmbed("**Bumble Bot positive phrases**", poss, Color.green).build()).queue();
			return true;
		}
		return false;
	}
}