package com.alyxferrari.bots.bumblecogs;
import java.awt.Color;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class FallbackModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;unlockdown")) {
			if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
				mevent.getGuild().getGuildChannelById(mevent.getChannel().getId()).getManager().putPermissionOverride(mevent.getGuild().getPublicRole(), Permission.MESSAGE_WRITE.getRawValue(), 0L).queue();
				mevent.getChannel().sendMessage(BumbleBot.getEmbed("**Channel unlock**", "Channel unlocked. 🟢", Color.green).build()).queue();
				return true;
			}
			mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
			return true;
		}
		return false;
	}
}