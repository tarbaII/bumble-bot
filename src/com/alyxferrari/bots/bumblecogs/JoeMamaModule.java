package com.alyxferrari.bots.bumblecogs;

import com.alyxferrari.bots.BotModule;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class JoeMamaModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentStripped().equalsIgnoreCase("joe")) {
			mevent.getChannel().sendMessage("joe mama").queue();
			return true;
		}
		return false;
	}
}