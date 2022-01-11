package com.alyxferrari.bots.bumblecogs;
import java.awt.Color;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class FallbackModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;")) {
			mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "That is not a valid command.", Color.red).build()).queue();
			return true;
		}
		return false;
	}
}