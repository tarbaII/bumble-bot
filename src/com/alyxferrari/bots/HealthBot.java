package com.alyxferrari.bots;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
public class HealthBot {
	public static String API_TOKEN = "";
	public static void start(String[] args) throws Exception {
		BufferedReader api = new BufferedReader(new InputStreamReader(new FileInputStream("healthapikey.txt")));
		API_TOKEN = api.readLine();
		api.close();
		JDABuilder builder = JDABuilder.createDefault(API_TOKEN);
		builder.setActivity(Activity.watching("over everyone"));
		builder.addEventListeners(new HealthListener());
		JDA jda = builder.build();
		jda.awaitReady();
		System.out.println("Logged in and ready (health)");
	}
	public static class HealthListener implements EventListener {
		@Override
		public void onEvent(GenericEvent event) {
			if (event instanceof MessageReceivedEvent) {
				MessageReceivedEvent mevent = (MessageReceivedEvent) event;
				if (!mevent.getAuthor().isBot()) {
					List<User> mentions = mevent.getMessage().getMentionedUsers();
					for (int i = 0; i < mentions.size(); i++) {
						if (mentions.get(i).getName().equalsIgnoreCase("Mental Health Assistant")) {
							mevent.getChannel().sendMessage("**Mental health resources:**\n\nThis page has information on many different hotlines:\n<https://www.pleaselive.org/hotlines/>\n\nThe Trevor Project:\n<https://www.thetrevorproject.org/get-help/>\n\nAlso feel free to ping or DM Billy (hamtarokujo#2309), who is also available to talk anytime.").queue();
						}
					}
				}
			}
		}
	}
}