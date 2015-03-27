package com.rhc.fundtracker.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TextFileManager {
	public static ArrayList<String> readFile(String file) {
		ArrayList<String> contents = new ArrayList<String>();
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = fileReader.readLine()) != null) {
				contents.add(line);
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contents;
	}

	public static boolean writeFile(String file, ArrayList<String> contents, boolean append) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));
			for (String line : contents) {
				writer.write(line);
				writer.newLine();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
}
