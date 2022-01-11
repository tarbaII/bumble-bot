package com.alyxferrari.bots.bumblecogs;
import com.alyxferrari.bots.BotModule;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class AmogusModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		double rand = Math.random() * 200.0;
		if (rand > 45.5 && rand < 46.5) {
			mevent.getChannel().sendMessage("amogus").queue();
		}
		return false;
	}
}