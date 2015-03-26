package controller;

import java.util.ArrayList;

import model.FundOperationCmd;
import model.FundSet;

public class Main {
	private ConfigReader configReader = null;
	private FundSet fundSet = null;
	private WebSpider webSpider = null;
	private WebPageParser webPageParser = null;

	private final String CONFIG_DIR = "./config/";

	public Main() {
		configReader = new ConfigReader();
		webSpider = new WebSpider(configReader);
		webPageParser = new WebPageParser(configReader);
	}

	public void init() {
		if (!configReader.init(CONFIG_DIR)) {
			System.out.println("error init");
			return;
		}
		fundSet = FundSet.parseFromBackup(configReader.getFundStatusFile());
		fundSet.addNewFunds(configReader.getFundsInfo());

		ArrayList<FundOperationCmd> cmdList = FundOperationCmd.parseFromFile(configReader.getFundOperationCmdFile());
		fundSet.addOperationCmds(cmdList);
		webSpider.setFundset(fundSet);
		
		System.out.println(fundSet.toString());
	}

	public void downloadPage() {
		webSpider.startCrawling();
		webSpider.writeToFile(configReader.getWebpageStoreFile());
	}

	public boolean parsePage() {
		return webPageParser.parsePage(fundSet, webSpider.getCrawlingDate());
	}

	public void storeStatus() {
		fundSet.writeToFile(configReader.getFundStatusFile());
	}

	public void printToConsole() {
		System.out.println(fundSet);
	}

	public static void main(String[] args) throws InterruptedException {
		Main m = new Main();
		m.init();
		while (true) {
			// Thread.sleep(5000);
			m.downloadPage();
			if (m.parsePage()) {
				m.storeStatus();
				m.printToConsole();
			}
		}
	}

}
