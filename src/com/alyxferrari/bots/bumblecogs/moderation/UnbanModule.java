package com.alyxferrari.bots.bumblecogs.moderation;
import java.awt.Color;
import java.util.function.Consumer;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class UnbanModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (mevent.getMessage().getContentRaw().startsWith("bb;unban") || mevent.getMessage().getContentRaw().startsWith("bb;pardon")) {
			if (mevent.getMember().hasPermission(Permission.ADMINISTRATOR) || mevent.getMember().getId().equals("322896756713062400")) {
				final String[] contents = mevent.getMessage().getContentStripped().split(" ");
				if (contents.length > 1) {
					try {
						Long.parseLong(contents[1]);
					} catch (NumberFormatException ex) {
						mevent.getChannel().sendMessage(BumbleBot.getEmbed(null, "Supplied argument is not a user ID.", Color.red).build()).queue();
						return true;
					}
					mevent.getJDA().retrieveUserById(contents[1]).queue(new Consumer<User>() {
						@Override
						public void accept(User user) {
							mevent.getGuild().unban(user).queue(new Consumer<Void>() {
								@Override
								public void accept(Void t) {
									mevent.getChannel().sendMessage(BumbleBot.getEmbed("Unban successful", "Successfully unbanned user with ID " + contents[1] + ".", Color.green).build()).queue();
								}
							}, new Consumer<Throwable>() {
								@Override
								public void accept(Throwable t) {
									t.printStackTrace();
									mevent.getChannel().sendMessage(BumbleBot.getEmbed("Unban failed", "Failed to unban user with ID " + contents[1] + ".\n\nException:\n" + t.getClass().getCanonicalName() + ": " + t.getMessage(), Color.red).build()).queue();
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
					mevent.getChannel().sendMessage(BumbleBot.getEmbed("**Insufficient arguments**", "Usage:\n`bb;unban <user ID>`", Color.red).build()).queue();
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
