package com.alyxferrari.bots.bumblecogs.bumblefuncs;
import java.awt.Color;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class ListNegativeModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;listneg")) {
			String negs = "";
			for (int i = 0; i < BumbleBot.neg.size(); i++) {
				negs += i == BumbleBot.neg.size()-1 ? BumbleBot.neg.get(i) : BumbleBot.neg.get(i) + "\n";
			}
			mevent.getChannel().sendMessage(BumbleBot.getEmbed("**Bumble Bot positive phrases**", negs, Color.green).build()).queue();
			return true;
		}
		return false;
	}
}