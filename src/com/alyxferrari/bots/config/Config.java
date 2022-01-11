package com.alyxferrari.bots.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Config {
	protected File file;
	protected HashMap<String, String> map = new HashMap<String, String>();
	public Config(File file) {
		this.file = file;
	}
	public Config load() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] components = line.split(":");
			if (components.length == 2) {
				map.put(components[0], components[1]);
			}
		}
		reader.close();
		return this;
	}
	public String query(String key) {
		return map.get(key);
	}
	public void save() throws IOException {
		file.delete();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (Map.Entry<String, String> entry : map.entrySet()) {
			writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
		}
		writer.flush();
		writer.close();
	}
}