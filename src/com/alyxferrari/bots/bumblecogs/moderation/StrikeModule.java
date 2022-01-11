package com.alyxferrari.bots.bumblecogs.moderation;
import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class StrikeModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;addstrike")) {
			if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
				final String[] contents = mevent.getMessage().getContentStripped().split(" ");
				if (contents.length > 1) {
					try {
						Long.parseLong(contents[1]);
					} catch (NumberFormatException ex) {
						mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Supplied argument is not a user ID.", Color.red).build()).queue();
						return true;
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
												mevent.getChannel().sendMessage(BumbleBot.getEmbed("Ban successful", "Successfully banned user with ID " + contents[1] + ".", Color.green).build()).queue();
											}
										}, new Consumer<Throwable>() {
											@Override
											public void accept(Throwable t) {
												System.out.println("poopy fard shid");
												mevent.getChannel().sendMessage(BumbleBot.getEmbed("Ban failed", "Failed to ban user with ID " + contents[1] + ".", Color.red).build()).queue();
											}
										});
									} catch (IllegalArgumentException ex) {
										if (ex.getMessage().equalsIgnoreCase("Deletion Days must not be bigger than 7.")) {
											mevent.getChannel().sendMessage(BumbleBot.getEmbed("Ban failed", "Deletion days must not be above 7 days.", Color.red).build()).queue();
										} else {
											mevent.getChannel().sendMessage(BumbleBot.getEmbed("Ban failed", "Failed to ban user with ID " + contents[1] + ".", Color.red).build()).queue();
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
												mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Successfully gave a strike to user with ID " + contents[1] + ".", Color.green).build()).queue();
												member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage("A moderator has given you a strike in channel `#" + mevent.getChannel().getName() + "`.\nHere is their explanation for their action:\n" + (finalReason.equals("") ? "No reason provided" : finalReason))).queue();
												// DM user explaining why
											}
										});
										List<Role> twoStrikes = mevent.getGuild().getRolesByName("two strikes", true);
										if (twoStrikes.size() > 0) {
											mevent.getGuild().removeRoleFromMember(member, twoStrikes.get(0)).queue();
										} else {
											mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Failed to remove role \"`two strikes`\" from user with ID " + contents[1] + ".", Color.red).build()).queue();
										}
									} else {
										mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Failed to add role \"`three strikes`\" to user with ID " + contents[1] + ".", Color.red).build()).queue();
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
												mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Successfully gave a strike to user with ID " + contents[1] + ".", Color.green).build()).queue();
												member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage("A moderator has given you a strike in channel `#" + mevent.getChannel().getName() + "`.\nHere is their explanation for their action:\n" + (finalReason.equals("") ? "No reason provided" : finalReason))).queue();
												// DM user explaining why
											}
										});
										List<Role> oneStrike = mevent.getGuild().getRolesByName("one strike", true);
										if (oneStrike.size() > 0) {
											mevent.getGuild().removeRoleFromMember(member, oneStrike.get(0)).queue();
										} else {
											mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Failed to remove role \"`one strike`\" from user with ID " + contents[1] + ".", Color.red).build()).queue();
										}
									} else {
										mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Failed to add role \"`two strikes`\" to user with ID " + contents[1] + ".", Color.red).build()).queue();
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
										mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Successfully gave a strike to user with ID " + contents[1] + ".", Color.green).build()).queue();
										member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage("A moderator has given you a strike in channel " + mevent.getChannel().getName() + ".\nHere is their explanation for their action:\n" + (finalReason.equals("") ? "No reason provided" : finalReason))).queue();
										// DM user explaining why
									}
								}, new Consumer<Throwable>() {
									@Override
									public void accept(Throwable t) {
										mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Failed to add role \"`one strike`\" to user with ID " + contents[1] + ".", Color.red).build()).queue();
									}
								});
							} else {
								mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Failed to add role \"`one strike`\" to user with ID " + contents[1] + ".", Color.red).build()).queue();
							}
							return;
						}
					}, new Consumer<Throwable>() {
						@Override
						public void accept(Throwable t) {
							mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "User could not be found.", Color.red).build()).queue();
						}
					});
				} else {
					mevent.getChannel().sendMessage(BumbleBot.getEmbed("**Insufficient arguments**", "Usage:\n`bb;addstrike <user ID> <reason>`", Color.red).build()).queue();
					return true;
				}
				return true;
			}
			mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "You don't have permission to do that.", Color.red).build()).queue();
			return true;
		}
		return false;
	}
}
