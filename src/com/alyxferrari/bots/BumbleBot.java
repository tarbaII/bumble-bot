package com.alyxferrari.bots;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.awt.Color;
import com.alyxferrari.bots.bumblecogs.AmogusModule;
import com.alyxferrari.bots.bumblecogs.EchoModule;
import com.alyxferrari.bots.bumblecogs.FallbackModule;
import com.alyxferrari.bots.bumblecogs.HelpModule;
import com.alyxferrari.bots.bumblecogs.JoeMamaModule;
import com.alyxferrari.bots.bumblecogs.LigmaModule;
import com.alyxferrari.bots.bumblecogs.QuitModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.AddNegativeModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.AddPositiveModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.BumbleBasedModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.BumbleTimerModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.DeleteNegativeModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.DeletePositiveModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.FuckYouModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.ListNegativeModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.ListPositiveModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.ReactionModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.SoTrueModule;
import com.alyxferrari.bots.bumblecogs.bumblefuncs.StatsModule;
import com.alyxferrari.bots.bumblecogs.moderation.BanModule;
import com.alyxferrari.bots.bumblecogs.moderation.KickModule;
import com.alyxferrari.bots.bumblecogs.moderation.LockdownModule;
import com.alyxferrari.bots.bumblecogs.moderation.StrikeModule;
import com.alyxferrari.bots.bumblecogs.moderation.UnbanModule;
import com.alyxferrari.bots.bumblecogs.moderation.UnlockdownModule;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
public class BumbleBot {
	public static String API_TOKEN = "";
	public static final ArrayList<String> messageIds = new ArrayList<String>();
	public static final ArrayList<String> pos = new ArrayList<String>();
	public static final ArrayList<String> neg = new ArrayList<String>();
	public static final BotModule[] commands = {
			new HelpModule(),
			new QuitModule(),
			new EchoModule(),
			new AddNegativeModule(),
			new AddPositiveModule(),
			new DeleteNegativeModule(),
			new DeletePositiveModule(),
			new ListNegativeModule(),
			new ListPositiveModule(),
			new StatsModule(),
			new BanModule(),
			new KickModule(),
			new UnbanModule(),
			new LockdownModule(),
			new UnlockdownModule(),
			new StrikeModule(),
			new AmogusModule.AmogusCommandModule(),
			new FallbackModule()
	};
	public static final BotModule[] nonReturning = {
			new ReactionModule(),
			new BumbleTimerModule(),
			new AmogusModule()
	};
	public static final BotModule[] returning = {
			new JoeMamaModule(),
			new LigmaModule(),
			new FuckYouModule(),
			new BumbleBasedModule(),
			new SoTrueModule()
	};
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
					for (int i = 0; i < commands.length; i++) {
						if (commands[i].execute(mevent)) return; // execute modules
					}
					for (int i = 0; i < nonReturning.length; i++) {
						nonReturning[i].execute(mevent);
					}
					for (int i = 0; i < returning.length; i++) {
						if (returning[i].execute(mevent)) return;
					}
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