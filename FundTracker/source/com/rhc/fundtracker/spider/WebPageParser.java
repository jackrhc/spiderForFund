package com.rhc.fundtracker.spider;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.rhc.fundtracker.fund.FundValue;

public class WebPageParser {
	private static Logger logger = Logger.getLogger(WebPageParser.class);
	private ConfigReader configReader = null;

	public WebPageParser(ConfigReader configReader) {
		this.configReader = configReader;
	}

	public ArrayList<FundValue> parseHistoryNetValue(String startDate, String endDate, ArrayList<String> contents) {
		if (contents.size() % 4 != 0) {
			logger.error("web page content error:" + contents.size());
			return null;
		}

		ArrayList<FundValue> fundValues = new ArrayList<FundValue>();
		for (int i = contents.size() - 1; i >= 0; i -= 4) {
			FundValue value = new FundValue();
			value.setDate(parseValueBetweenAngleBrackets(contents.get(i - 3)));
			value.setNetValue(parseValueBetweenAngleBrackets(contents.get(i - 2)));
			value.setAggregateValue(parseValueBetweenAngleBrackets(contents.get(i - 1)));
			value.setGrowthRate(parseValueBetweenAngleBrackets(contents.get(i)));
			fundValues.add(value);
		}
		return fundValues;
	}

	private String parseValueBetweenAngleBrackets(String line) {
		int leftAngleBracketPos = line.indexOf('>');
		int rightAngleBracketPos = line.indexOf('<', leftAngleBracketPos + 1);
		return line.substring(leftAngleBracketPos + 1, rightAngleBracketPos);
	}
}
