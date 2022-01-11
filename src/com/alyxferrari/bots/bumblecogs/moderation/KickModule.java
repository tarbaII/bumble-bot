package com.alyxferrari.bots.bumblecogs.moderation;
import java.awt.Color;
import java.util.function.Consumer;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class KickModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;kick")) {
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
							String reason = "";
							if (contents.length > 2) {
								for (int i = 2; i < contents.length; i++) {
									reason += contents[i] + " ";
								}
							}
							mevent.getGuild().kick(member, reason.equals("") ? "No reason provided" : reason).queue(new Consumer<Void>() {
								@Override
								public void accept(Void t) {
									mevent.getChannel().sendMessage(BumbleBot.getEmbed("Kick successful", "Successfully kicked user with ID " + contents[1] + ".", Color.green).build()).queue();
								}
							}, new Consumer<Throwable>() {
								@Override
								public void accept(Throwable t) {
									mevent.getChannel().sendMessage(BumbleBot.getEmbed("Kick failed", "Failed to kick user with ID " + contents[1] + ".", Color.red).build()).queue();
								}
							});
						}
					}, new Consumer<Throwable>() {
						@Override
						public void accept(Throwable t) {
							mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "User could not be found.", Color.red).build()).queue();
						}
					});
				} else {
					mevent.getChannel().sendMessage(BumbleBot.getEmbed("**Insufficient arguments**", "Usage:\n`bb;kick <user ID> <reason>`", Color.red).build()).queue();
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