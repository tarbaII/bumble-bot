package com.alyxferrari.bots.bumblecogs;
import com.alyxferrari.bots.BotModule;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class EchoModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;say") || mevent.getMessage().getContentRaw().startsWith("bb;echo")) {
			String say = "";
			String[] contents = mevent.getMessage().getContentRaw().split(" ");
			if (contents.length < 2 || !mevent.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				if (!mevent.getMember().getId().equals("322896756713062400")) {
					mevent.getMessage().delete().queue();
					return true;
				}
			}
			for (int i = 1; i < contents.length; i++) {
				say += contents[i] + " ";
			}
			mevent.getMessage().delete().queue();
			mevent.getChannel().sendMessage(say).queue();
			return true;
		}
		return false;
	}
}