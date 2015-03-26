package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigReader {
	private static final String FUNDS_LIST_FILENAME = "fundsList.txt";
	private static final String CONFIG_FILENAME = "config.txt";
	private ArrayList<String> fundsInfo = null;

	private String crawlingUri = null;
	private String subStrOfRequiredContent = null;
	private String webpageStoreFile = null;
	private String fundStatusFile = null;
	private String fundOperationCmdFile = null;
	private String fundHistoryValueDir = null;
	
	private String splitRegex = null;
	private int indexOfFundId = 0;
	private int indexOfFundName = 0;
	private int indexOfTodayNetValue = 0;
	private int indexOfTodayAggregateValue = 0;
	private int indexOfYesterdayNetValue = 0;
	private int indexOfYesterdayAggregateValue = 0;

	public ConfigReader() {
		fundsInfo = new ArrayList<String>();
	}

	public boolean init(String configDir) {
		String fundsListFile = configDir + FUNDS_LIST_FILENAME;
		String configFile = configDir + CONFIG_FILENAME;
		try {
			readFundList(fundsListFile);
			readConfig(configFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private void readFundList(String fundsListFile) throws IOException {
		BufferedReader fundsReader = new BufferedReader(new FileReader(fundsListFile));
		String line = null;
		while ((line = fundsReader.readLine()) != null) {
			fundsInfo.add(line);
		}
		fundsReader.close();
	}

	private void readConfig(String configFile) throws IOException {
		BufferedReader configReader = new BufferedReader(new FileReader(configFile));
		String line = null;
		while ((line = configReader.readLine()) != null) {
			if (line.isEmpty() || line.charAt(0) == '#') continue;

			String[] fields = line.split("=");
			if (fields[0].compareTo("crawlingUri") == 0) {
				this.crawlingUri = fields[1];
			} else if (fields[0].compareTo("subStrOfRequiredContent") == 0) {
				this.subStrOfRequiredContent = fields[1];
			} else if (fields[0].compareTo("splitRegex") == 0) {
				this.splitRegex = fields[1];
			} else if (fields[0].compareTo("indexOfFundId") == 0) {
				this.indexOfFundId = Integer.valueOf(fields[1]);
			} else if (fields[0].compareTo("indexOfFundName") == 0) {
				this.indexOfFundName = Integer.valueOf(fields[1]);
			} else if (fields[0].compareTo("indexOfTodayNetValue") == 0) {
				this.indexOfTodayNetValue = Integer.valueOf(fields[1]);
			} else if (fields[0].compareTo("indexOfTodayAggregateValue") == 0) {
				this.indexOfTodayAggregateValue = Integer.valueOf(fields[1]);
			} else if (fields[0].compareTo("indexOfYesterdayNetValue") == 0) {
				this.indexOfYesterdayNetValue = Integer.valueOf(fields[1]);
			} else if (fields[0].compareTo("indexOfYesterdayAggregateValue") == 0) {
				this.indexOfYesterdayAggregateValue = Integer.valueOf(fields[1]);
			} else if (fields[0].compareTo("webpageStoreFile") == 0) {
				this.webpageStoreFile = fields[1];
			} else if (fields[0].compareTo("fundStatusFile") == 0) {
				this.fundStatusFile = fields[1];
			} else if (fields[0].compareTo("fundOperationCmdFile") == 0) {
				this.fundOperationCmdFile = fields[1];
			} else if (fields[0].compareTo("fundHistoryValueDir") == 0) {
				this.fundHistoryValueDir = fields[1];
			} 
		}
		configReader.close();
	}

	public ArrayList<String> getFundsInfo() {
		return fundsInfo;
	}

	public String getCrawlingUri() {
		return crawlingUri;
	}

	public String getSubStrOfRequiredContent() {
		return subStrOfRequiredContent;
	}

	public String getSplitRegex() {
		return splitRegex;
	}

	public int getIndexOfFundId() {
		return indexOfFundId;
	}

	public int getIndexOfFundName() {
		return indexOfFundName;
	}

	public int getIndexOfTodayNetValue() {
		return indexOfTodayNetValue;
	}

	public int getIndexOfTodayAggregateValue() {
		return indexOfTodayAggregateValue;
	}

	public int getIndexOfYesterdayNetValue() {
		return indexOfYesterdayNetValue;
	}

	public int getIndexOfYesterdayAggregateValue() {
		return indexOfYesterdayAggregateValue;
	}

	public String getWebpageStoreFile() {
		return webpageStoreFile;
	}

	public String getFundStatusFile() {
		return fundStatusFile;
	}

	public String getFundOperationCmdFile() {
		return fundOperationCmdFile;
	}

	public String getFundHistoryValueDir() {
		return fundHistoryValueDir;
	}

	public void setFundHistoryValueDir(String fundHistoryValueDir) {
		this.fundHistoryValueDir = fundHistoryValueDir;
	}
}
