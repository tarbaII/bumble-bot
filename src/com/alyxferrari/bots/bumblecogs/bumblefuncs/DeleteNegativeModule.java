package com.alyxferrari.bots.bumblecogs.bumblefuncs;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class DeleteNegativeModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;delneg")) {
			if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
				String[] parts = mevent.getMessage().getContentRaw().split(" ");
				if (parts.length > 1) {
					String toAdd = "";
					for (int i = 1; i < parts.length; i++) {
						toAdd += i == parts.length-1 ? parts[i] : parts[i] + " ";
					}
					if (BumbleBot.neg.remove(toAdd)) {
						new File("bumbleneg.txt").delete();
						try {
							BufferedWriter writer = new BufferedWriter(new FileWriter(new File("bumbleneg.txt")));
							String negs = "";
							for (int i = 0; i < BumbleBot.neg.size(); i++) {
								negs += i == BumbleBot.neg.size()-1 ? BumbleBot.neg.get(i) : BumbleBot.neg.get(i) + "\n";
							}
							writer.write(negs);
							writer.flush();
							writer.close();
						} catch (IOException ex) {
							ex.printStackTrace();
							mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Failed to remove from negative phrase list.", Color.red).build()).queue();
						}
						mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Removed from negative phrase list.", Color.green).build()).queue();
					} else {
						mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "That phrase was not in the list.", Color.red).build()).queue();
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