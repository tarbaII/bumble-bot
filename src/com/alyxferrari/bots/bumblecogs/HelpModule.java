package com.alyxferrari.bots.bumblecogs;
import java.awt.Color;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class HelpModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;help")) {
			mevent.getChannel().sendMessage(BumbleBot.getEmbed("**Bumble Bot help**", "`bb;stats` : Shows how many times Bumble Bot has said the positive and negative messages\n`bb;listpos` : Lists positive phrases\n`bb;listneg` : Lists negative phrases", Color.green).build()).queue();
			return true;
		}
		return false;
	}
}