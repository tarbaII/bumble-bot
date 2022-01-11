package com.alyxferrari.bots.bumblecogs;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import com.alyxferrari.bots.config.ConfigList;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class AmogusModule implements BotModule {
	public static HashMap<Long, ConfigList> configs = new HashMap<Long, ConfigList>();
	public AmogusModule() {
		try {
			File[] files = new File("configs/").listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].getName().startsWith("AmogusConfig-") && files[i].getName().endsWith(".txt")) {
					try {
						long guild = Long.parseLong(files[i].getName().split("-")[1].split(".")[0]);
						configs.put(guild, new ConfigList(files[i]).load());
					} catch (NumberFormatException ex) {} catch (ArrayIndexOutOfBoundsException ex) {}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("AmogusModule failed to load.");
		}
	}
	public boolean execute(MessageReceivedEvent mevent) {
		ConfigList config = configs.get(mevent.getGuild().getIdLong());
		if (config == null || !config.contains(mevent.getChannel().getName())) {
			double rand = Math.random() * 200.0;
			if (rand > 45.5 && rand < 46.5) {
				mevent.getChannel().sendMessage("amogus").queue();
			}
		}
		return false;
	}
	public static class AmogusCommandModule implements BotModule {
		public boolean execute(MessageReceivedEvent mevent) {
			if (mevent.getMessage().getContentRaw().startsWith("bb;addamoguschannelexception")) {
				String[] components = mevent.getMessage().getContentDisplay().split(" ");
				if (components.length == 2) {
					String channel = components[1];
					if (channel.startsWith("#")) channel = channel.split("#")[0];
					ConfigList config = configs.get(mevent.getGuild().getIdLong());
					if (config == null) {
						
					} else {
						config.addElement(channel);
						configs.put(mevent.getGuild().getIdLong(), config);
					}
					return true;
				} else {
					mevent.getChannel().sendMessage(BumbleBot.getEmbed("**Insufficient arguments**", "Usage:\n`bb;addamoguschannelexception <channel>`", Color.red).build()).queue();
					return true;
				}
			} else if (mevent.getMessage().getContentRaw().startsWith("bb;delamoguschannelexception")) {
				
			}
			return false;
		}
	}
}