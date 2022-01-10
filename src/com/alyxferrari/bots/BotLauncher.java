package com.alyxferrari.bots;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class BotLauncher {
	public static void main(String[] args) throws Exception {
		Thread bumble = new BumbleThread(args);
		bumble.start();
		Thread health = new HealthThread(args);
		health.start();
		if (args.length == 0) {
			JFrame frame = new JFrame("alyx bot");
			frame.setSize(400, 200);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(BorderLayout.CENTER, new JLabel("alyx bot started"));
			frame.setVisible(true);
		}
	}
	public static class BumbleThread extends Thread {
		String[] args;
		public BumbleThread(String[] args) {
			this.args = args;
		}
		@Override
		public void run() {
			try {
				BumbleBot.start(args);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	public static class HealthThread extends Thread {
		String[] args;
		public HealthThread(String[] args) {
			this.args = args;
		}
		@Override
		public void run() {
			try {
				HealthBot.start(args);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}