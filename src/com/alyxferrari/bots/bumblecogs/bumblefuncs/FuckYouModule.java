package com.alyxferrari.bots.bumblecogs.bumblefuncs;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class FuckYouModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		for (int i = 0; i < BumbleBot.neg.size(); i++) {
			if (StringUtils.containsIgnoreCase(mevent.getMessage().getContentStripped(), BumbleBot.neg.get(i))) {
				mevent.getChannel().sendMessage("fuck you").queue();
				BumbleBot.fuck++;
				new File("bumblecounter.txt").delete();
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(new File("bumblecounter.txt")));
					writer.write(BumbleBot.based + "\n" + BumbleBot.fuck);
					writer.flush();
					writer.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return true;
			}
		}
		if (mevent.getMessage().getContentStripped().startsWith("fuck you") || mevent.getMessage().getContentStripped().startsWith("Fuck you") || mevent.getMessage().getContentStripped().equalsIgnoreCase("fuck you")) {
			MessageChannel channel = mevent.getChannel();
			channel.getHistoryBefore(mevent.getMessage(), 1).queue(new Consumer<MessageHistory>() {
				@Override
				public void accept(MessageHistory history) {
					List<Message> messages = history.getRetrievedHistory();
					if (messages.size() == 1) {
						if (messages.get(0).getAuthor().getName().equals("AlyxBot")) {
							mevent.getChannel().sendMessage("well fuck you too then").queue();
						}
					}
				}
			});
		}
		return false;
	}
}