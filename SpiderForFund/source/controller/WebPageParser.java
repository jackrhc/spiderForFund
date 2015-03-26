package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.FundSet;

public class WebPageParser {
	private ConfigReader configReader = null;

	public WebPageParser(ConfigReader configReader) {
		this.configReader = configReader;
	}

	public boolean parsePage(FundSet fundSet, String crawlingDate) {
		boolean result = false;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(configReader.getWebpageStoreFile()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] arr = line.split(configReader.getSplitRegex());
				String fundId = arr[configReader.getIndexOfFundId()];
				if (!fundSet.isChoosed(fundId)) continue;

				String fundName = arr[configReader.getIndexOfFundName()];
				if (arr[configReader.getIndexOfTodayNetValue()].compareTo("--") != 0) {
					double netValue = Double.valueOf(arr[configReader.getIndexOfTodayNetValue()]);
					double aggregateValue = Double.valueOf(arr[configReader.getIndexOfTodayAggregateValue()]);
					if (fundSet.updateFundStatus(crawlingDate, fundId, fundName, netValue, aggregateValue)) {
						result = true;
					}
				}
				if (arr[configReader.getIndexOfYesterdayNetValue()].compareTo("--") != 0) {
					double netValue = Double.valueOf(arr[configReader.getIndexOfYesterdayNetValue()]);
					double aggregateValue = Double.valueOf(arr[configReader.getIndexOfYesterdayAggregateValue()]);
					if (fundSet.updateFundStatus(getYesterdayStr(crawlingDate), fundId, fundName, netValue, aggregateValue)) {
						result = true;
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
