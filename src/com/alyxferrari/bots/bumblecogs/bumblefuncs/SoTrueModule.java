package com.alyxferrari.bots.bumblecogs.bumblefuncs;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import com.alyxferrari.bots.BotModule;
import com.alyxferrari.bots.BumbleBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class SoTrueModule implements BotModule {
	public boolean execute(MessageReceivedEvent mevent) {
		if (StringUtils.containsIgnoreCase(mevent.getMessage().getContentStripped(), "bumble is based")) {
			mevent.getChannel().sendMessage("so true bestie").queue();
			BumbleBot.based++;
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
		return false;
	}
}