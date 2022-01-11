package com.alyxferrari.bots;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public interface BotModule {
	public boolean execute(MessageReceivedEvent mevent);
}