package com.rhc.fundtracker.fund;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rhc.fundtracker.spider.ConfigReader;
import com.rhc.fundtracker.spider.WebPageParser;
import com.rhc.fundtracker.spider.WebSpider;
import com.rhc.fundtracker.util.CalendarConverter;
import com.rhc.fundtracker.util.TextFileManager;

public class FundHoldings {
	private Map<String, Fund> fundMap = null;
	private ConfigReader configReader = null;
	private WebSpider spider = null;
	private WebPageParser pageParser = null;

	public FundHoldings(ConfigReader configReader, WebSpider webSpider, WebPageParser webPageParser) {
		this.configReader = configReader;
		this.spider = webSpider;
		this.pageParser = webPageParser;
	}

	public void loadFunds(String filePath) {
		fundMap = new HashMap<String, Fund>();

		ArrayList<String> contents = TextFileManager.readFile(filePath);
		for (String line : contents) {
			String fundName = line.substring(0, line.length() - 6);
			String fundId = line.substring(line.length() - 6);
			fundMap.put(fundId, new Fund(fundId, fundName));
		}
	}

	public void addOperationCmds(ArrayList<FundOperationCmd> cmdList) {
		for (FundOperationCmd cmd : cmdList) {
			Fund fund = fundMap.get(cmd.getFundId());
			fund.addOperationCmd(cmd);
		}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(Fund.getHeaderStr()).append('\n');
		for (String id : fundMap.keySet()) {
			Fund fund = fundMap.get(id);
			builder.append(fund).append('\n');
		}
		return builder.toString();
	}

	public void updateFundHistoryNetValue() {
		String currentDate = CalendarConverter.getCurrentDate();
		for (Fund fund : fundMap.values()) {
			String netValueFilePath = configReader.getFundNetValueDir() + fund.getFileName() + ".txt";
			if (TextFileManager.isFileExist(netValueFilePath)) {
				fund.readNetValueFromFile(netValueFilePath);
			}
			// 如果历史净值还有完全获取到，则先用爬虫爬取
			if (CalendarConverter.dayDiff(currentDate, fund.getLastestUpdateDate()) > 1) {
				String startDate = CalendarConverter.getAnotherDay(fund.getLastestUpdateDate(), 1);
				ArrayList<String> contents = spider.crawlingHistoryNetValue(fund.getId(), startDate, currentDate);
				ArrayList<FundDayValue> values = pageParser.parseHistoryNetValue(startDate, currentDate, contents);
				fund.addFundValueList(values);
				fund.writeNetValueToFile(values, netValueFilePath);
			}
		}
	}
}
