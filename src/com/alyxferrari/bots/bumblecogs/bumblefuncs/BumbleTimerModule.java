package com.alyxferrari.bots.bumblecogs.bumblefuncs;

import java.time.OffsetDateTime;

import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BumbleTimerModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getMember().getId().equals("275116146426904577")) {
			OffsetDateTime created = mevent.getMessage().getTimeCreated();
			if (BumbleBot.lastBumbleMessage != null) {
				if (created.getYear() != BumbleBot.lastBumbleMessage.getYear() || created.getDayOfMonth() != BumbleBot.lastBumbleMessage.getDayOfMonth() || BumbleBot.difference(BumbleBot.lastBumbleMessage.getHour(), created.getHour()) > 6) {
					mevent.getChannel().sendMessage("babe wake up bumble sent a new message").queue();
				}
			}
			BumbleBot.lastBumbleMessage = mevent.getMessage().getTimeCreated();
		}
		return false;
	}
}