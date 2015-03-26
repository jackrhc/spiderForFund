package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FundSet {
	private Map<String, Fund> fundMap = null;

	public static FundSet parseFromBackup(String file) {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FundSet fundSet = new FundSet();
		if (builder.length() < 3) fundSet.setFundByJSONObject(new JSONObject());
		else fundSet.setFundByJSONObject(JSON.parseObject(builder.toString()));
		return fundSet;
	}

	public void addNewFunds(ArrayList<String> fundInfo) {
		for (String info : fundInfo) {
			info = info.trim();
			String fundName = info.substring(0, info.length() - 6);
			String fundId = info.substring(info.length() - 6);
			if (fundMap.containsKey(fundId)) continue;
			fundMap.put(fundId, new Fund(fundId, fundName));
		}
	}

	public boolean updateFundStatus(String crawlingDate, String id, String name, double netValue, double aggregateValue) {
		Fund fund = fundMap.get(id);
		if (fund.getName().compareTo(name) != 0) {
			System.out.println("add fund error" + id + " " + name);
			return false;
		}

		return fund.updateFundValue(crawlingDate, netValue, aggregateValue);
	}

	public void addOperationCmds(ArrayList<FundOperationCmd> cmdList) {
		for (FundOperationCmd cmd : cmdList) {
			Fund fund = fundMap.get(cmd.getFundId());
			fund.addOperationCmd(cmd);
		}
	}

	public void writeToFile(String file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(JSON.toJSONString(fundMap, true));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
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

	public Map<String, Fund> getFundMap() {
		return fundMap;
	}

	public String[] getFundIdList() {
		return fundMap.keySet().toArray(new String[0]);
	}

	public void setFundMap(Map<String, Fund> fundMap) {
		this.fundMap = fundMap;
	}

	public boolean isChoosed(String fundId) {
		return fundMap.containsKey(fundId);
	}

	public void setFundByJSONObject(JSONObject fundJSON) {
		this.fundMap = new HashMap<String, Fund>();
		for (String id : fundJSON.keySet()) {
			Fund fund = fundJSON.getObject(id, Fund.class);
			this.fundMap.put(id, fund.Clone());
		}
	}
}
