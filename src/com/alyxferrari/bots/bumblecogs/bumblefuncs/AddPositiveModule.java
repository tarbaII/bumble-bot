package com.alyxferrari.bots.bumblecogs.bumblefuncs;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class AddPositiveModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;addpos")) {
			if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
				String[] parts = mevent.getMessage().getContentRaw().split(" ");
				if (parts.length > 1) {
					String toAdd = "";
					for (int i = 1; i < parts.length; i++) {
						toAdd += i == parts.length-1 ? parts[i] : parts[i] + " ";
					}
					BumbleBot.pos.add(toAdd);
					try {
						PrintWriter writer = new PrintWriter(new FileOutputStream("bumblepos.txt"));
						writer.println(toAdd);
						writer.flush();
						writer.close();
						mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Added to positive phrase list.", Color.green).build()).queue();
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
						mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Failed to add to positive phrase list.", Color.red).build()).queue();
					}
				}
			} else {
				mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
			}
			return true;
		}
		return false;
	}
}