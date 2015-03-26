package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import model.FundSet;

public class WebSpider {
	private String content = null;
	private ConfigReader configReader = null;
	private FundSet fundset = null;
	private String crawlingDate = null; // 爬取页面对应的日期

	public WebSpider(ConfigReader configReader) {
		this.configReader = configReader;
	}
	
	public boolean startCrawling() {
		HttpURLConnection httpConn = null;

		try {
			for (String fundId : fundset.getFundIdList()) {
				// http://jingzhi.funds.hexun.com/DataBase/jzzs.aspx?fundcode=000697&startdate=2015-02-12&enddate=2015-03-24
				String webPageUrl = configReader.getCrawlingUri() + fundId;
				URL url = new URL(webPageUrl);
				httpConn = (HttpURLConnection) url.openConnection();
				httpConn.setRequestMethod("GET");
				httpConn.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");
				BufferedReader bf = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));

				String line = null;
				StringBuilder builder = new StringBuilder();
				while ((line = bf.readLine()) != null) {
					int datePos = line.indexOf("行情日期");
					if (datePos != -1) {
						crawlingDate = line.substring(datePos + 5, datePos + 15);
					}
					if (line.indexOf(configReader.getSubStrOfRequiredContent()) == -1) continue;

					builder.append(line);
					builder.append('\n');
				}
				content = builder.toString();
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return false;
		} finally {
			httpConn.disconnect();
		}
		return true;
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

	public String getCrawlingDate() {
		return crawlingDate;
	}

	public FundSet getFundset() {
		return fundset;
	}

	public void setFundset(FundSet fundset) {
		this.fundset = fundset;
	}

}
