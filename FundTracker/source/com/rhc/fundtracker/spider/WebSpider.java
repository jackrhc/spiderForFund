package com.rhc.fundtracker.spider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.rhc.fundtracker.fund.FundHoldings;

public class WebSpider {
	private String content = null;
	private ConfigReader configReader = null;
	private FundHoldings fundset = null;

	public WebSpider(ConfigReader configReader) {
		this.configReader = configReader;
	}

	public ArrayList<String> crawlingHistoryNetValue(String fundId, String startDate, String endDate) {
		HttpURLConnection httpConn = null;

		try {
			String webPageUrl = configReader.getCrawlingUri();
			webPageUrl = webPageUrl.replace("{FUND_ID}", fundId);
			webPageUrl = webPageUrl.replace("{S_DATE}", startDate);
			webPageUrl = webPageUrl.replace("{D_DATE}", endDate);

			URL url = new URL(webPageUrl);
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");
			BufferedReader bf = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));

			String line = null;
			ArrayList<String> result = new ArrayList<String>();
			while ((line = bf.readLine()) != null) {
				if (line.indexOf(configReader.getSubStrOfRequiredContent()) >= 0) {
					result.add(line);
				}
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			httpConn.disconnect();
		}
		return null;
	}

	public void writeToFile(String filePath) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public FundHoldings getFundset() {
		return fundset;
	}

	public void setFundset(FundHoldings fundset) {
		this.fundset = fundset;
	}

}
