package com.alyxferrari.bots.bumblecogs.bumblefuncs;
import java.awt.Color;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class StatsModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;stats")) {
			mevent.getChannel().sendMessage(BumbleBot.getEmbed("**Bumble Bot stats**", "`bumble is based` : " + BumbleBot.based + "\n`fuck you` : " + BumbleBot.fuck, Color.green).build()).queue();
			return true;
		}
		return false;
	}
}