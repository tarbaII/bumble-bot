package com.alyxferrari.bots;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
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
import java.awt.Color;
import org.apache.commons.lang3.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.Role;
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
	public static void start(String[] args) throws Exception {
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
		builder.setActivity(Activity.playing("with farts"));
		builder.addEventListeners(new BumbleListener());
		JDA jda = builder.build();
		jda.awaitReady();
		System.out.println("Logged in and ready");
	}
	public static class BumbleListener implements EventListener {
		@Override
		public void onEvent(GenericEvent event) {
			if (event instanceof MessageReceivedEvent) {
				MessageReceivedEvent mevent = (MessageReceivedEvent) event;
				if (!mevent.getAuthor().isBot()) {
					if (mevent.getMessage().getContentRaw().startsWith("bb;addneg")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
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
									mevent.getChannel().sendMessage(getEmbed(null, "Added to negative phrase list.", Color.green).build()).queue();
								} catch (FileNotFoundException ex) {
									ex.printStackTrace();
									mevent.getChannel().sendMessage(getEmbed(null, "Failed to add to negative phrase list.", Color.red).build()).queue();
								}
							}
						} else {
							mevent.getChannel().sendMessage(getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
						}
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;addpos")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
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
									mevent.getChannel().sendMessage(getEmbed(null, "Added to positive phrase list.", Color.green).build()).queue();
								} catch (FileNotFoundException ex) {
									ex.printStackTrace();
									mevent.getChannel().sendMessage(getEmbed(null, "Failed to add to positive phrase list.", Color.red).build()).queue();
								}
							}
						} else {
							mevent.getChannel().sendMessage(getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
						}
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;delpos")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
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
										mevent.getChannel().sendMessage(getEmbed(null, "Failed to remove from positive phrase list.", Color.red).build()).queue();
									}
									mevent.getChannel().sendMessage(getEmbed(null, "Removed from positive phrase list.", Color.green).build()).queue();
								} else {
									mevent.getChannel().sendMessage(getEmbed(null, "That phrase was not in the list.", Color.red).build()).queue();
								}
							}
						} else {
							mevent.getChannel().sendMessage(getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
						}
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;delneg")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
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
										mevent.getChannel().sendMessage(getEmbed(null, "Failed to remove from negative phrase list.", Color.red).build()).queue();
									}
									mevent.getChannel().sendMessage(getEmbed(null, "Removed from negative phrase list.", Color.green).build()).queue();
								} else {
									mevent.getChannel().sendMessage(getEmbed(null, "That phrase was not in the list.", Color.red).build()).queue();
								}
							}
						} else {
							mevent.getChannel().sendMessage(getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
						}
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;lockdown")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
							mevent.getGuild().getGuildChannelById(mevent.getChannel().getId()).getManager().putPermissionOverride(mevent.getGuild().getPublicRole(), 0L, Permission.MESSAGE_WRITE.getRawValue()).queue();
							mevent.getChannel().sendMessage(getEmbed("**Channel lockdown**", "Channel locked down. 🔴", Color.red).build()).queue();
							return;
						}
						mevent.getChannel().sendMessage(getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;unlockdown")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
							mevent.getGuild().getGuildChannelById(mevent.getChannel().getId()).getManager().putPermissionOverride(mevent.getGuild().getPublicRole(), Permission.MESSAGE_WRITE.getRawValue(), 0L).queue();
							mevent.getChannel().sendMessage(getEmbed("**Channel unlock**", "Channel unlocked. 🟢", Color.green).build()).queue();
							return;
						}
						mevent.getChannel().sendMessage(getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;listpos")) {
						String poss = "";
						for (int i = 0; i < pos.size(); i++) {
							poss += i == pos.size()-1 ? pos.get(i) : pos.get(i) + "\n";
						}
						mevent.getChannel().sendMessage(getEmbed("**Bumble Bot positive phrases**", poss, Color.green).build()).queue();
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;listneg")) {
						String negs = "";
						for (int i = 0; i < neg.size(); i++) {
							negs += i == neg.size()-1 ? neg.get(i) : neg.get(i) + "\n";
						}
						mevent.getChannel().sendMessage(getEmbed("**Bumble Bot positive phrases**", negs, Color.green).build()).queue();
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;quit") || mevent.getMessage().getContentRaw().startsWith("bb;exit")) {
						mevent.getChannel().sendMessage(getEmbed(null, "Quitting... 🛑", Color.red).build()).queue();
						try {Thread.sleep(500);} catch (InterruptedException ex) {}
						System.exit(0);
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;ban")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
							final String[] contents = mevent.getMessage().getContentStripped().split(" ");
							if (contents.length > 2) {
								try {
									Long.parseLong(contents[1]);
								} catch (NumberFormatException ex) {
									mevent.getChannel().sendMessage(getEmbed(null, "Supplied argument is not a user ID.", Color.red).build()).queue();
									return;
								}
								try {
									Integer.parseInt(contents[2]);
								} catch (NumberFormatException ex) {
									mevent.getChannel().sendMessage(getEmbed(null, "Supplied deletion period is not an integer.", Color.red).build()).queue();
									return;
								}
								mevent.getJDA().retrieveUserById(contents[1]).queue(new Consumer<User>() {
									@Override
									public void accept(User user) {
										String reason = "";
										if (contents.length > 3) {
											for (int i = 3; i < contents.length; i++) {
												reason += contents[i] + " ";
											}
										}
										try {
											mevent.getGuild().ban(user, Integer.parseInt(contents[2]), reason.equals("") ? "No reason provided" : reason).queue(new Consumer<Void>() {
												@Override
												public void accept(Void t) {
													mevent.getChannel().sendMessage(getEmbed("Ban successful", "Successfully banned user with ID " + contents[1] + ".", Color.green).build()).queue();
												}
											}, new Consumer<Throwable>() {
												@Override
												public void accept(Throwable t) {
													t.printStackTrace();
													mevent.getChannel().sendMessage(getEmbed("Ban failed", "Failed to ban user with ID " + contents[1] + ".\n\nException:\n" + t.getClass().getCanonicalName() + ": " + t.getMessage(), Color.red).build()).queue();
												}
											});
										} catch (IllegalArgumentException ex) {
											if (ex.getMessage().equalsIgnoreCase("Deletion Days must not be bigger than 7.")) {
												mevent.getChannel().sendMessage(getEmbed("Ban failed", "Deletion days must not be above 7 days.", Color.red).build()).queue();
											} else {
												mevent.getChannel().sendMessage(getEmbed("Ban failed", "Failed to ban user with ID " + contents[1] + ".", Color.red).build()).queue();
											}
											return;
										}
									}
								}, new Consumer<Throwable>() {
									@Override
									public void accept(Throwable t) {
										mevent.getChannel().sendMessage(getEmbed(null, "User could not be found.", Color.red).build()).queue();
									}
								});
							} else {
								mevent.getChannel().sendMessage(getEmbed("**Insufficient arguments**", "Usage:\n`bb;ban <user ID> <message delete days> <reason>`", Color.red).build()).queue();
								return;
							}
							return;
						}
						mevent.getChannel().sendMessage(getEmbed(null, "You don't have permission to do that", Color.red).build()).queue();
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;unban") || mevent.getMessage().getContentRaw().startsWith("bb;pardon")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
							final String[] contents = mevent.getMessage().getContentStripped().split(" ");
							if (contents.length > 1) {
								try {
									Long.parseLong(contents[1]);
								} catch (NumberFormatException ex) {
									mevent.getChannel().sendMessage(getEmbed(null, "Supplied argument is not a user ID.", Color.red).build()).queue();
									return;
								}
								mevent.getJDA().retrieveUserById(contents[1]).queue(new Consumer<User>() {
									@Override
									public void accept(User user) {
										mevent.getGuild().unban(user).queue(new Consumer<Void>() {
											@Override
											public void accept(Void t) {
												mevent.getChannel().sendMessage(getEmbed("Unban successful", "Successfully unbanned user with ID " + contents[1] + ".", Color.green).build()).queue();
											}
										}, new Consumer<Throwable>() {
											@Override
											public void accept(Throwable t) {
												t.printStackTrace();
												mevent.getChannel().sendMessage(getEmbed("Unban failed", "Failed to unban user with ID " + contents[1] + ".\n\nException:\n" + t.getClass().getCanonicalName() + ": " + t.getMessage(), Color.red).build()).queue();
											}
										});
									}
								}, new Consumer<Throwable>() {
									@Override
									public void accept(Throwable t) {
										mevent.getChannel().sendMessage(getEmbed(null, "User could not be found.", Color.red).build()).queue();
									}
								});
							} else {
								mevent.getChannel().sendMessage(getEmbed("**Insufficient arguments**", "Usage:\n`bb;unban <user ID>`", Color.red).build()).queue();
								return;
							}
							return;
						}
						mevent.getChannel().sendMessage(getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;kick")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
							final String[] contents = mevent.getMessage().getContentStripped().split(" ");
							if (contents.length > 1) {
								try {
									Long.parseLong(contents[1]);
								} catch (NumberFormatException ex) {
									mevent.getChannel().sendMessage(getEmbed(null, "Supplied argument is not a user ID.", Color.red).build()).queue();
									return;
								}
								mevent.getGuild().retrieveMemberById(contents[1]).queue(new Consumer<Member>() {
									@Override
									public void accept(Member member) {
										String reason = "";
										if (contents.length > 2) {
											for (int i = 2; i < contents.length; i++) {
												reason += contents[i] + " ";
											}
										}
										mevent.getGuild().kick(member, reason.equals("") ? "No reason provided" : reason).queue(new Consumer<Void>() {
											@Override
											public void accept(Void t) {
												mevent.getChannel().sendMessage(getEmbed("Kick successful", "Successfully kicked user with ID " + contents[1] + ".", Color.green).build()).queue();
											}
										}, new Consumer<Throwable>() {
											@Override
											public void accept(Throwable t) {
												mevent.getChannel().sendMessage(getEmbed("Kick failed", "Failed to kick user with ID " + contents[1] + ".", Color.red).build()).queue();
											}
										});
									}
								}, new Consumer<Throwable>() {
									@Override
									public void accept(Throwable t) {
										mevent.getChannel().sendMessage(getEmbed(null, "User could not be found.", Color.red).build()).queue();
									}
								});
							} else {
								mevent.getChannel().sendMessage(getEmbed("**Insufficient arguments**", "Usage:\n`bb;kick <user ID> <reason>`", Color.red).build()).queue();
								return;
							}
							return;
						}
						mevent.getChannel().sendMessage(getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;addstrike")) {
						if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
							final String[] contents = mevent.getMessage().getContentStripped().split(" ");
							if (contents.length > 1) {
								try {
									Long.parseLong(contents[1]);
								} catch (NumberFormatException ex) {
									mevent.getChannel().sendMessage(getEmbed(null, "Supplied argument is not a user ID.", Color.red).build()).queue();
									return;
								}
								mevent.getGuild().retrieveMemberById(contents[1]).queue(new Consumer<Member>() {
									@Override
									public void accept(Member member) {
										List<Role> roles = member.getRoles();
										for (int i = 0; i < roles.size(); i++) {
											if (roles.get(i).getName().equalsIgnoreCase("three strikes")) {
												System.out.println("fart shid");
												String reason = "";
												if (contents.length > 2) {
													for (int x = 2; x < contents.length; x++) {
														reason += contents[x] + " ";
													}
												}
												System.out.println("shid fard");
												try {
													mevent.getGuild().ban(member, 0, reason.equals("") ? "No reason provided" : reason).queue(new Consumer<Void>() {
														@Override
														public void accept(Void t) {
															System.out.println("poopy doop");
															mevent.getChannel().sendMessage(getEmbed("Ban successful", "Successfully banned user with ID " + contents[1] + ".", Color.green).build()).queue();
														}
													}, new Consumer<Throwable>() {
														@Override
														public void accept(Throwable t) {
															System.out.println("poopy fard shid");
															mevent.getChannel().sendMessage(getEmbed("Ban failed", "Failed to ban user with ID " + contents[1] + ".", Color.red).build()).queue();
														}
													});
												} catch (IllegalArgumentException ex) {
													if (ex.getMessage().equalsIgnoreCase("Deletion Days must not be bigger than 7.")) {
														mevent.getChannel().sendMessage(getEmbed("Ban failed", "Deletion days must not be above 7 days.", Color.red).build()).queue();
													} else {
														mevent.getChannel().sendMessage(getEmbed("Ban failed", "Failed to ban user with ID " + contents[1] + ".", Color.red).build()).queue();
													}
													return;
												}
												return;
											}
										}
										for (int i = 0; i < roles.size(); i++) {
											if (roles.get(i).getName().equalsIgnoreCase("two strikes")) {
												List<Role> threeStrikes = mevent.getGuild().getRolesByName("three strikes", true);
												if (threeStrikes.size() > 0) {
													String reason = "";
													if (contents.length > 2) {
														for (int x = 2; x < contents.length; x++) {
															reason += contents[x] + " ";
														}
													}
													final String finalReason = reason;
													mevent.getGuild().addRoleToMember(member, threeStrikes.get(0)).queue(new Consumer<Void>() {
														@Override
														public void accept(Void t) {
															mevent.getChannel().sendMessage(getEmbed(null, "Successfully gave a strike to user with ID " + contents[1] + ".", Color.green).build()).queue();
															member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage("A moderator has given you a strike in channel `#" + mevent.getChannel().getName() + "`.\nHere is their explanation for their action:\n" + (finalReason.equals("") ? "No reason provided" : finalReason))).queue();
															// DM user explaining why
														}
													});
													List<Role> twoStrikes = mevent.getGuild().getRolesByName("two strikes", true);
													if (twoStrikes.size() > 0) {
														mevent.getGuild().removeRoleFromMember(member, twoStrikes.get(0)).queue();
													} else {
														mevent.getChannel().sendMessage(getEmbed(null, "Failed to remove role \"`two strikes`\" from user with ID " + contents[1] + ".", Color.red).build()).queue();
													}
												} else {
													mevent.getChannel().sendMessage(getEmbed(null, "Failed to add role \"`three strikes`\" to user with ID " + contents[1] + ".", Color.red).build()).queue();
												}
												return;
											}
										}
										for (int i = 0; i < roles.size(); i++) {
											if (roles.get(i).getName().equalsIgnoreCase("one strike")) {
												List<Role> twoStrikes = mevent.getGuild().getRolesByName("two strikes", true);
												if (twoStrikes.size() > 0) {
													String reason = "";
													if (contents.length > 2) {
														for (int x = 2; x < contents.length; x++) {
															reason += contents[x] + " ";
														}
													}
													final String finalReason = reason;
													mevent.getGuild().addRoleToMember(member, twoStrikes.get(0)).queue(new Consumer<Void>() {
														@Override
														public void accept(Void t) {
															mevent.getChannel().sendMessage(getEmbed(null, "Successfully gave a strike to user with ID " + contents[1] + ".", Color.green).build()).queue();
															member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage("A moderator has given you a strike in channel `#" + mevent.getChannel().getName() + "`.\nHere is their explanation for their action:\n" + (finalReason.equals("") ? "No reason provided" : finalReason))).queue();
															// DM user explaining why
														}
													});
													List<Role> oneStrike = mevent.getGuild().getRolesByName("one strike", true);
													if (oneStrike.size() > 0) {
														mevent.getGuild().removeRoleFromMember(member, oneStrike.get(0)).queue();
													} else {
														mevent.getChannel().sendMessage(getEmbed(null, "Failed to remove role \"`one strike`\" from user with ID " + contents[1] + ".", Color.red).build()).queue();
													}
												} else {
													mevent.getChannel().sendMessage(getEmbed(null, "Failed to add role \"`two strikes`\" to user with ID " + contents[1] + ".", Color.red).build()).queue();
												}
												return;
											}
										}
										List<Role> oneStrike = mevent.getGuild().getRolesByName("one strike", true);
										if (oneStrike.size() > 0) {
											String reason = "";
											if (contents.length > 2) {
												for (int x = 2; x < contents.length; x++) {
													reason += contents[x] + " ";
												}
											}
											final String finalReason = reason;
											mevent.getGuild().addRoleToMember(member, oneStrike.get(0)).queue(new Consumer<Void>() {
												@Override
												public void accept(Void t) {
													mevent.getChannel().sendMessage(getEmbed(null, "Successfully gave a strike to user with ID " + contents[1] + ".", Color.green).build()).queue();
													member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage("A moderator has given you a strike in channel " + mevent.getChannel().getName() + ".\nHere is their explanation for their action:\n" + (finalReason.equals("") ? "No reason provided" : finalReason))).queue();
													// DM user explaining why
												}
											}, new Consumer<Throwable>() {
												@Override
												public void accept(Throwable t) {
													mevent.getChannel().sendMessage(getEmbed(null, "Failed to add role \"`one strike`\" to user with ID " + contents[1] + ".", Color.red).build()).queue();
												}
											});
										} else {
											mevent.getChannel().sendMessage(getEmbed(null, "Failed to add role \"`one strike`\" to user with ID " + contents[1] + ".", Color.red).build()).queue();
										}
										return;
									}
								}, new Consumer<Throwable>() {
									@Override
									public void accept(Throwable t) {
										mevent.getChannel().sendMessage(getEmbed(null, "User could not be found.", Color.red).build()).queue();
									}
								});
							} else {
								mevent.getChannel().sendMessage(getEmbed("**Insufficient arguments**", "Usage:\n`bb;addstrike <user ID> <reason>`", Color.red).build()).queue();
								return;
							}
							return;
						}
						mevent.getChannel().sendMessage(getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;say")) {
						String say = "";
						String[] contents = mevent.getMessage().getContentRaw().split(" ");
						if (contents.length < 2 || !mevent.getMember().hasPermission(Permission.ADMINISTRATOR)) {
							if (!mevent.getMember().getId().equals("322896756713062400")) {
								mevent.getMessage().delete().queue();
								return;
							}
						}
						for (int i = 1; i < contents.length; i++) {
							say += contents[i] + " ";
						}
						mevent.getMessage().delete().queue();
						mevent.getChannel().sendMessage(say).queue();
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;stats")) {
						mevent.getChannel().sendMessage(getEmbed("**Bumble Bot stats**", "`bumble is based` : " + based + "\n`fuck you` : " + fuck, Color.green).build()).queue();
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;help")) {
						mevent.getChannel().sendMessage(getEmbed("**Bumble Bot help**", "`bb;stats` : Shows how many times Bumble Bot has said the positive and negative messages\n`bb;listpos` : Lists positive phrases\n`bb;listneg` : Lists negative phrases", Color.green).build()).queue();
						return;
					} else if (mevent.getMessage().getContentRaw().startsWith("bb;")) {
						mevent.getChannel().sendMessage(getEmbed("**Unknown command**", "Run `bb;help` for a list of commands.", Color.red).build()).queue();
					}
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
					for (int i = 0; i < neg.size(); i++) {
						if (StringUtils.containsIgnoreCase(mevent.getMessage().getContentStripped(), neg.get(i))) {
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
					if (StringUtils.containsIgnoreCase(mevent.getMessage().getContentStripped(), "bumble is based")) {
						mevent.getChannel().sendMessage("so true bestie").queue();
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
						return;
					}
					if (mevent.getMessage().getContentStripped().equalsIgnoreCase("bumble")) {
						mevent.getChannel().sendMessage("is based").queue();
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
						return;
					}
					for (int i = 0; i < pos.size(); i++) {
						if (StringUtils.containsIgnoreCase(mevent.getMessage().getContentStripped(), pos.get(i))) {
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
							return;
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
					if (mevent.getMessage().getContentStripped().equalsIgnoreCase("joe")) {
						mevent.getChannel().sendMessage("joe mama").queue();
					}
					if (mevent.getMessage().getContentStripped().equalsIgnoreCase("i can't believe steve jobs died of ligma") || mevent.getMessage().getContentStripped().equalsIgnoreCase("i cant believe steve jobs died of ligma") || mevent.getMessage().getContentStripped().equalsIgnoreCase("who the hell is steve jobs")) {
						mevent.getChannel().sendMessage("ligma balls").queue();
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
				}
			}
		}
		public static EmbedBuilder getEmbed(String title, String text, Color color) {
			EmbedBuilder ret = new EmbedBuilder();
			if (color != null) {
				ret.setColor(color);
			}
			if (title != null && !title.equals("")) {
				ret.setTitle(title);
			}
			if (text != null && !text.equals("")) {
				ret.setDescription(text);
			}
			return ret;
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