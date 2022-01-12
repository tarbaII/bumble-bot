package com.alyxferrari.bots.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ConfigList {
	protected File file;
	protected ArrayList<String> list = new ArrayList<String>();
	public ConfigList(File file) {
		this.file = file;
	}
	public ConfigList load() throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
			reader.close();
		} catch (FileNotFoundException ex) {}
		return this;
	}
	public ArrayList<String> getList() {
		return list;
	}
	public String getElement(int index) {
		return list.get(index);
	}
	public void addElement(String element) {
		list.add(element);
	}
	public void removeElement(String element) {
		list.remove(element);
	}
	public boolean contains(String value) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).contains(value)) return true;
		}
		return false;
	}
	public void save() throws IOException {
		file.delete();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < list.size(); i++) {
			writer.write(list.get(i) + "\n");
		}
		writer.flush();
		writer.close();
	}
}