package com.alyxferrari.bumblebot;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.OffsetDateTime;

import org.apache.commons.lang3.StringUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
public class BumbleBot {
	public static String API_TOKEN = "";
	public static final ArrayList<String> messageIds = new ArrayList<String>();
	public static final ArrayList<String> pos = new ArrayList<String>();
	public static final ArrayList<String> neg = new ArrayList<String>();
	public static int based = 0;
	public static int fuck = 0;
	public static OffsetDateTime lastBumbleMessage = null;
	public static void main(String[] args) throws Exception {
		BufferedReader pos = new BufferedReader(new InputStreamReader(new FileInputStream("bumblepos.txt")));
		BufferedReader neg = new BufferedReader(new InputStreamReader(new FileInputStream("bumbleneg.txt")));
		BufferedReader counter = new BufferedReader(new InputStreamReader(new FileInputStream("bumblecounter.txt")));
		BufferedReader api = new BufferedReader(new InputStreamReader(new FileInputStream("apikey.txt")));
		String line;
		while ((line = pos.readLine()) != null) {
			BumbleBot.pos.add(line);
		}
		while ((line = neg.readLine()) != null) {
			BumbleBot.neg.add(line);
		}
		based = Integer.parseInt(counter.readLine());
		fuck = Integer.parseInt(counter.readLine());
		API_TOKEN = api.readLine();
		api.close();
		counter.close();
		pos.close();
		neg.close();
		JDABuilder builder = JDABuilder.createDefault(API_TOKEN);
		builder.setActivity(Activity.watching("over Bumble"));
		builder.addEventListeners(new BumbleListener());
		JDA jda = builder.build();
		jda.awaitReady();
		System.out.println("Logged in and ready");
		JFrame frame = new JFrame("bumble bot");
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.CENTER, new JLabel("bumble bot started"));
		frame.setVisible(true);
	}
	public static class BumbleListener implements EventListener {
		@Override
		public void onEvent(GenericEvent event) {
			if (event instanceof MessageReceivedEvent) {
				MessageReceivedEvent mevent = (MessageReceivedEvent) event;
				if (!mevent.getAuthor().isBot()) {
					if (mevent.getMessage().getContentRaw().startsWith("bb;addneg")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR)) {
							String[] parts = mevent.getMessage().getContentRaw().split(" ");
							if (parts.length > 1) {
								String toAdd = "";
								for (int i = 1; i < parts.length; i++) {
									toAdd += i == parts.length-1 ? parts[i] : parts[i] + " ";
								}
								neg.add(toAdd);
								try {
									PrintWriter writer = new PrintWriter(new FileOutputStream("bumbleneg.txt"));
									writer.println(toAdd);
									writer.flush();
									writer.close();
									mevent.getChannel().sendMessage("Added to negative phrase list.").queue();
								} catch (FileNotFoundException ex) {
									ex.printStackTrace();
									mevent.getChannel().sendMessage("Failed to add to negative phrase list.").queue();
								}
							}
						} else {
							mevent.getChannel().sendMessage("You don't have permission to do that.").queue();
						}
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;addpos")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR)) {
							String[] parts = mevent.getMessage().getContentRaw().split(" ");
							if (parts.length > 1) {
								String toAdd = "";
								for (int i = 1; i < parts.length; i++) {
									toAdd += i == parts.length-1 ? parts[i] : parts[i] + " ";
								}
								pos.add(toAdd);
								try {
									PrintWriter writer = new PrintWriter(new FileOutputStream("bumblepos.txt"));
									writer.println(toAdd);
									writer.flush();
									writer.close();
									mevent.getChannel().sendMessage("Added to positive phrase list.").queue();
								} catch (FileNotFoundException ex) {
									ex.printStackTrace();
									mevent.getChannel().sendMessage("Failed to add to positive phrase list.").queue();
								}
							}
						} else {
							mevent.getChannel().sendMessage("You don't have permission to do that.").queue();
						}
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;delpos")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR)) {
							String[] parts = mevent.getMessage().getContentRaw().split(" ");
							if (parts.length > 1) {
								String toAdd = "";
								for (int i = 1; i < parts.length; i++) {
									toAdd += i == parts.length-1 ? parts[i] : parts[i] + " ";
								}
								if (pos.remove(toAdd)) {
									new File("bumbleneg.txt").delete();
									try {
										BufferedWriter writer = new BufferedWriter(new FileWriter(new File("bumbleneg.txt")));
										String negs = "";
										for (int i = 0; i < neg.size(); i++) {
											negs += i == neg.size()-1 ? neg.get(i) : neg.get(i) + "\n";
										}
										writer.write(negs);
										writer.flush();
										writer.close();
									} catch (IOException ex) {
										ex.printStackTrace();
										mevent.getChannel().sendMessage("Failed to remove from positive phrase list.").queue();
									}
									mevent.getChannel().sendMessage("Removed.").queue();
								} else {
									mevent.getChannel().sendMessage("That phrase was not in the list in the first place.").queue();
								}
							}
						} else {
							mevent.getChannel().sendMessage("You don't have permission to do that.").queue();
						}
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;delneg")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR)) {
							String[] parts = mevent.getMessage().getContentRaw().split(" ");
							if (parts.length > 1) {
								String toAdd = "";
								for (int i = 1; i < parts.length; i++) {
									toAdd += i == parts.length-1 ? parts[i] : parts[i] + " ";
								}
								if (neg.remove(toAdd)) {
									new File("bumbleneg.txt").delete();
									try {
										BufferedWriter writer = new BufferedWriter(new FileWriter(new File("bumbleneg.txt")));
										String negs = "";
										for (int i = 0; i < neg.size(); i++) {
											negs += i == neg.size()-1 ? neg.get(i) : neg.get(i) + "\n";
										}
										writer.write(negs);
										writer.flush();
										writer.close();
									} catch (IOException ex) {
										ex.printStackTrace();
										mevent.getChannel().sendMessage("Failed to remove from positive phrase list.").queue();
									}
									mevent.getChannel().sendMessage("Removed.").queue();
								} else {
									mevent.getChannel().sendMessage("That phrase was not in the list in the first place.").queue();
								}
							}
						} else {
							mevent.getChannel().sendMessage("You don't have permission to do that.").queue();
						}
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;stats")) {
						mevent.getChannel().sendMessage("**Bumble Bot stats:**\n`bumble is based` : " + based + "\n`fuck you` : " + fuck).queue();
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;help")) {
						mevent.getChannel().sendMessage("**Bumble Bot help:**\n`bb;addpos phrase` : Adds `phrase` to positive phrase list\n`bb;addneg phrase` : Adds `phrase` to negative phrase list\n`bb;delpos phrase` : Removes `phrase` from positive phrase list\n`bb;delneg phrase` : Removes `phrase` from negative phrase list\n`bb;stats` : Shows how many times Bumble Bot has said the positive and negative messages").queue();
						return;
					}
					for (int i = 0; i < neg.size(); i++) {
						if (StringUtils.containsIgnoreCase(mevent.getMessage().getContentRaw(), neg.get(i))) {
							mevent.getChannel().sendMessage("fuck you").queue();
							fuck++;
							new File("bumblecounter.txt").delete();
							try {
								BufferedWriter writer = new BufferedWriter(new FileWriter(new File("bumblecounter.txt")));
								writer.write(based + "\n" + fuck);
								writer.flush();
								writer.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
							return;
						}
					}
					for (int i = 0; i < pos.size(); i++) {
						if (StringUtils.containsIgnoreCase(mevent.getMessage().getContentRaw(), pos.get(i))) {
							mevent.getChannel().sendMessage("bumble is based").queue();
							based++;
							new File("bumblecounter.txt").delete();
							try {
								BufferedWriter writer = new BufferedWriter(new FileWriter(new File("bumblecounter.txt")));
								writer.write(based + "\n" + fuck);
								writer.flush();
								writer.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					}
					List<User> mentioned = mevent.getMessage().getMentionedUsers();
					for (int i = 0; i < mentioned.size(); i++) {
						if (mentioned.get(i).getName().equals("Bumble Bot")) {
							List<Emote> bumbles = mevent.getGuild().getEmotesByName("bumble", false);
							System.out.println(mevent.getGuild().getEmotesByName("heartpulse", false).size());
							if (bumbles.size() > 0) {
								mevent.getMessage().addReaction(bumbles.get(0)).queue();
							} else {
								mevent.getMessage().addReaction("U+2764");
							}
						}
					}
					if (mevent.getMessage().getMember().getId().equals("275116146426904577")) {
						OffsetDateTime created = mevent.getMessage().getTimeCreated();
						if (lastBumbleMessage != null) {
							if (created.getYear() != lastBumbleMessage.getYear() || created.getDayOfMonth() != lastBumbleMessage.getDayOfMonth() || difference(lastBumbleMessage.getHour(), created.getHour()) > 6) {
								mevent.getChannel().sendMessage("babe wake up bumble sent a new message").queue();
							}
						}
						lastBumbleMessage = mevent.getMessage().getTimeCreated();
					}
					if (mevent.getMessage().getContentRaw().equalsIgnoreCase("joe")) {
						mevent.getChannel().sendMessage("joe mama").queue();
					}
					if (mevent.getMessage().getContentRaw().equalsIgnoreCase("i can't believe steve jobs died of ligma") || mevent.getMessage().getContentRaw().equalsIgnoreCase("i cant believe steve jobs died of ligma") || mevent.getMessage().getContentRaw().equalsIgnoreCase("who the hell is steve jobs")) {
						mevent.getChannel().sendMessage("ligma balls").queue();
					}
				}
			}
		}
		public static int difference(int hour1, int hour2) {
			if (hour2 > hour1) {
				return hour2-hour1;
			} else if (hour2 < hour1) {
				int ret = 0;
				for (int i = hour1; i < 24; i++) {
					ret++;
				}
				for (int i = 0; i < hour2; i++) {
					ret++;
				}
				return ret;
			}
			return 0;
		}
	}
}