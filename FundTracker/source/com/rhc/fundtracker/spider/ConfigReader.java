package com.rhc.fundtracker.spider;

import java.util.ArrayList;

import com.rhc.fundtracker.util.TextFileManager;

public class ConfigReader {
	private static final String FUNDS_LIST_FILENAME = "fundsList.txt";
	private static final String CONFIG_FILENAME = "config.txt";
	private String fundsListFile = null;
	private String crawlingUri = null;
	private String subStrOfRequiredContent = null;
	private String webpageStoreFile = null;
	private String fundNetValueDir = null;
	private String fundOperationDir = null;
	private String fundOperationCmdFile = null;

	public ConfigReader() {
	}

	public boolean init(String configDir) {
		fundsListFile = configDir + FUNDS_LIST_FILENAME;
		String configFile = configDir + CONFIG_FILENAME;
		ArrayList<String> contents = TextFileManager.readFile(configFile);
		if (contents.isEmpty()) return false;
		for (String line : contents) {
			if (line.isEmpty() || line.charAt(0) == '#') continue;

			int pos = line.indexOf('=');
			String fieldName = line.substring(0, pos);
			String fieldValue = line.substring(pos + 1);
			if (fieldName.compareTo("crawlingUri") == 0) {
				this.crawlingUri = fieldValue;
			} else if (fieldName.compareTo("subStrOfRequiredContent") == 0) {
				this.subStrOfRequiredContent = fieldValue;
			} else if (fieldName.compareTo("webpageStoreFile") == 0) {
				this.webpageStoreFile = fieldValue;
			} else if (fieldName.compareTo("fundNetValueDir") == 0) {
				this.fundNetValueDir = fieldValue;
			} else if (fieldName.compareTo("fundOperationDir") == 0) {
				this.fundOperationDir = fieldValue;
			} else if (fieldName.compareTo("fundOperationCmdFile") == 0) {
				this.fundOperationCmdFile = fieldValue;
			} 
		}
		return true;
	}

	public String getCrawlingUri() {
		return crawlingUri;
	}

	public String getSubStrOfRequiredContent() {
		return subStrOfRequiredContent;
	}

	public String getWebpageStoreFile() {
		return webpageStoreFile;
	}

	public String getFundNetValueDir() {
		return fundNetValueDir;
	}

	public String getFundOperationDir() {
		return fundOperationDir;
	}

	public String getFundsListFile() {
		return fundsListFile;
	}

	public void setFundsListFile(String fundsListFile) {
		this.fundsListFile = fundsListFile;
	}

	public String getFundOperationCmdFile() {
		return fundOperationCmdFile;
	}

	public void setFundOperationCmdFile(String fundOperationCmdFile) {
		this.fundOperationCmdFile = fundOperationCmdFile;
	}
}
