package com.alyxferrari.bots.bumblecogs;
import com.alyxferrari.bots.BotModule;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class LigmaModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentStripped().equalsIgnoreCase("i can't believe steve jobs died of ligma") || mevent.getMessage().getContentStripped().equalsIgnoreCase("i cant believe steve jobs died of ligma") || mevent.getMessage().getContentStripped().equalsIgnoreCase("who the hell is steve jobs") || mevent.getMessage().getContentStripped().equalsIgnoreCase("it's so sad steve jobs died from ligma") || mevent.getMessage().getContentStripped().equalsIgnoreCase("its so sad steve jobs died from ligma") || mevent.getMessage().getContentStripped().equalsIgnoreCase("it's so sad steve jobs died of ligma") || mevent.getMessage().getContentStripped().equalsIgnoreCase("its so sad steve jobs died of ligma")) {
			mevent.getChannel().sendMessage("ligma balls").queue();
			return true;
		}
		return false;
	}
}