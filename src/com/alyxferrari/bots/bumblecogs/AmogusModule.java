package com.alyxferrari.bots.bumblecogs;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import com.alyxferrari.bots.config.ConfigList;

import net.dv8tion.jda.api.Permission;
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
			if (mevent.getMessage().getContentRaw().contains("amogus") || (rand > 45.5 && rand < 46.5)) {
				mevent.getChannel().sendMessage("amogus").queue();
			}
		}
		return false;
	}
	public static class AmogusCommandModule implements BotModule {
		public boolean execute(MessageReceivedEvent mevent) {
			if (mevent.getMessage().getContentRaw().startsWith("bb;addamoguschannelexception")) {
				if (mevent.getMessage().getMember().hasPermission(Permission.ADMINISTRATOR)) {
					String[] components = mevent.getMessage().getContentDisplay().split(" ");
					if (components.length == 2) {
						String channel = components[1];
						if (channel.startsWith("#")) channel = channel.split("#")[0];
						ConfigList config = configs.get(mevent.getGuild().getIdLong());
						if (config == null) {
							config = new ConfigList(new File("configs/AmogusConfig-" + mevent.getGuild().getIdLong() + ".txt"));
						}
						config.addElement(channel);
						try {
							config.save();
							mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Successfully added channel exception to AmogusModule.", Color.green).build()).queue();
						} catch (IOException ex) {
							ex.printStackTrace();
							mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Failed to add channel exception to AmogusModule.", Color.red).build()).queue();
						}
						configs.put(mevent.getGuild().getIdLong(), config);
					} else {
						mevent.getChannel().sendMessage(BumbleBot.getEmbed("**Insufficient arguments**", "Usage:\n`bb;addamoguschannelexception <channel>`", Color.red).build()).queue();
					}
				} else {
					mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
				}
				return true;
			} else if (mevent.getMessage().getContentRaw().startsWith("bb;delamoguschannelexception")) {
				String[] components = mevent.getMessage().getContentDisplay().split(" ");
				if (components.length == 2) {
					String channel = components[1];
					if (channel.startsWith("#")) channel = channel.split("#")[0];
					ConfigList config = configs.get(mevent.getGuild().getIdLong());
					if (config == null) {
						mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "That channel was not in the exceptions list.", Color.red).build()).queue();
						return true;
					} else {
						config.removeElement(channel);
						try {
							config.save();
							mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Successfully removed channel exception from AmogusModule.", Color.green).build()).queue();
						} catch (IOException ex) {
							ex.printStackTrace();
							mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Failed to remove channel exception from AmogusModule.", Color.red).build()).queue();
						}
						configs.put(mevent.getGuild().getIdLong(), config);
					}
				}
				return true;
			}
			return false;
		}
	}
}