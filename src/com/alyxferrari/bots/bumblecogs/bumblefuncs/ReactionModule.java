package com.alyxferrari.bots.bumblecogs.bumblefuncs;
import java.util.List;
import com.alyxferrari.bots.BotModule;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class ReactionModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		List<User> mentioned = mevent.getMessage().getMentionedUsers();
		for (int i = 0; i < mentioned.size(); i++) {
			if (mentioned.get(i).getName().equals("AlyxBot")) {
				List<Emote> bumbles = mevent.getGuild().getEmotesByName("bumble", false);
				if (bumbles.size() > 0) {
					mevent.getMessage().addReaction(bumbles.get(0)).queue();
				} else {
					mevent.getMessage().addReaction("U+2764");
				}
			}
		}
		return false;
	}
}