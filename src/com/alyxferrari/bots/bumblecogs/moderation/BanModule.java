package com.alyxferrari.bots.bumblecogs.moderation;
import java.awt.Color;
import java.util.function.Consumer;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class BanModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;ban")) {
			if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
				final String[] contents = mevent.getMessage().getContentStripped().split(" ");
				if (contents.length > 2) {
					try {
						Long.parseLong(contents[1]);
					} catch (NumberFormatException ex) {
						mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Supplied argument is not a user ID.", Color.red).build()).queue();
						return true;
					}
					try {
						Integer.parseInt(contents[2]);
					} catch (NumberFormatException ex) {
						mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Supplied deletion period is not an integer.", Color.red).build()).queue();
						return true;
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
										mevent.getChannel().sendMessage(BumbleBot.getEmbed("Ban successful", "Successfully banned user with ID " + contents[1] + ".", Color.green).build()).queue();
									}
								}, new Consumer<Throwable>() {
									@Override
									public void accept(Throwable t) {
										t.printStackTrace();
										mevent.getChannel().sendMessage(BumbleBot.getEmbed("Ban failed", "Failed to ban user with ID " + contents[1] + ".\n\nException:\n" + t.getClass().getCanonicalName() + ": " + t.getMessage(), Color.red).build()).queue();
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
						}
					}, new Consumer<Throwable>() {
						@Override
						public void accept(Throwable t) {
							mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "User could not be found.", Color.red).build()).queue();
						}
					});
				} else {
					mevent.getChannel().sendMessage(BumbleBot.getEmbed("**Insufficient arguments**", "Usage:\n`bb;ban <user ID> <message delete days> <reason>`", Color.red).build()).queue();
					return true;
				}
				return true;
			}
			mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "You don't have permission to do that", Color.red).build()).queue();
			return true;
		}
		return false;
	}
}