package com.alyxferrari.bots.bumblecogs;
import java.awt.Color;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class QuitModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;quit") || mevent.getMessage().getContentRaw().startsWith("bb;exit")) {
			mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Quitting... 🛑", Color.red).build()).queue();
			try {Thread.sleep(500);} catch (InterruptedException ex) {}
			System.exit(0);
			return true;
		}
		return false;
	}
}