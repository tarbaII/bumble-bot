package com.alyxferrari.bots;
public class BotLauncher {
	public static void main(String[] args) throws Exception {
		Thread bumble = new BumbleThread(args);
		bumble.start();
		Thread health = new HealthThread(args);
		health.start();
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