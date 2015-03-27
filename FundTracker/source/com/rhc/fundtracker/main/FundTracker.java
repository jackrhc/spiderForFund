package com.rhc.fundtracker.main;

import java.util.ArrayList;

import com.rhc.fundtracker.fund.FundHoldings;
import com.rhc.fundtracker.fund.FundOperationCmd;
import com.rhc.fundtracker.spider.ConfigReader;
import com.rhc.fundtracker.spider.WebPageParser;
import com.rhc.fundtracker.spider.WebSpider;

public class FundTracker {
	private ConfigReader configReader = null;
	private FundHoldings fundHoldings = null;
	private WebSpider webSpider = null;
	private WebPageParser webPageParser = null;

	private final String CONFIG_DIR = "./config/";

	public FundTracker() {
		configReader = new ConfigReader();
		webSpider = new WebSpider(configReader);
		webPageParser = new WebPageParser();
		fundHoldings = new FundHoldings(configReader, webSpider, webPageParser);
	}

	public void init() {
		if (!configReader.init(CONFIG_DIR)) { return; }

		ArrayList<FundOperationCmd> cmdList = FundOperationCmd.parseFromFile(configReader.getFundOperationCmdFile());
		fundHoldings.loadFunds(configReader.getFundsListFile());
		fundHoldings.addOperationCmds(cmdList);
		fundHoldings.updateFundHistoryNetValue();
	}

	public static void main(String[] args) throws InterruptedException {
		FundTracker tracker = new FundTracker();
		tracker.init();
		while (true) {
			// Thread.sleep(5000);
		}
	}

}
