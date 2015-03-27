package com.rhc.fundtracker.fund;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.rhc.fundtracker.fund.FundOperationCmd.CmdType;
import com.rhc.fundtracker.util.CalendarConverter;
import com.rhc.fundtracker.util.TextFileManager;

public class Fund implements Cloneable {
	private static Logger logger = Logger.getLogger(Fund.class);
	private String id = "";
	private String name = "";

	private String lastestUpdateDate = "2014-12-01"; // 最近一次更新对应日期
	private Map<String, FundDayValue> fundValueMap = null; // 时间到基金净值的映射
	private ArrayList<FundOperationCmd> operationCmds = null; // 待操作的序列
	private LinkedList<HoldingValue> holdingValues = null; // 该基金持有的份额数及对应买的时候的净值

	private double upToNowProfit = 0.0;
	private final double BUY_RATE = 0.006; // 申购费率（暂时不考虑不同平台不一样的情况）
	private final double SELL_RATE = 0.005; // 赎回费率

	public Fund(String id, String name) {
		this.id = id;
		this.name = name;
		fundValueMap = new HashMap<String, FundDayValue>();
		operationCmds = new ArrayList<FundOperationCmd>();
		holdingValues = new LinkedList<HoldingValue>();
	}

	public void readNetValueFromFile(String filePath) {
		ArrayList<String> contents = TextFileManager.readFile(filePath);
		for (String line : contents) {
			FundDayValue value = FundDayValue.parse(line);
			fundValueMap.put(value.getToday(), value);
			if (lastestUpdateDate.compareTo(value.getToday()) < 0) {
				lastestUpdateDate = value.getToday();
			}
		}
	}

	public void writeNetValueToFile(ArrayList<FundDayValue> values, String filePath) {
		ArrayList<String> valuesStr = new ArrayList<String>(values.size());
		for (FundDayValue value : values) {
			valuesStr.add(value.toString());
		}
		TextFileManager.writeFile(filePath, valuesStr, true);
	}

	public void addOperationCmd(FundOperationCmd cmd) {
		operationCmds.add(cmd);
	}

	public String getFileName() {
		return this.id + "_" + this.name;
	}

	public static String getHeaderStr() {
		return "name\t\ttodayWorth\tyestdayWorth\tupToNowProfit\tquantity\tupdateData\tvalue\t\tcmd\t\t";
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append('\t');
		builder.append(lastestUpdateDate).append("\t\t");
		for (FundOperationCmd cmd : operationCmds) {
			builder.append(cmd).append(',');
		}
		return builder.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<FundOperationCmd> getOperationCmds() {
		return operationCmds;
	}

	public String getLastestUpdateDate() {
		return lastestUpdateDate;
	}

	public void setLastestUpdateDate(String lastestUpdateDate) {
		this.lastestUpdateDate = lastestUpdateDate;
	}

	public double getUpToNowProfit() {
		return upToNowProfit;
	}

	public void setUpToNowProfit(double upToNowProfit) {
		this.upToNowProfit = upToNowProfit;
	}

	public void addFundValueList(ArrayList<FundDayValue> values) {
		FundDayValue earliestFundValue = values.get(values.size() - 1);
		int dayDiff = CalendarConverter.dayDiff(earliestFundValue.getToday(), this.lastestUpdateDate);
		if (!(dayDiff > 0 && dayDiff < 10)) {
			logger.error("diff time error:" + dayDiff);
			return;
		}
		for (FundDayValue value : values) {
			addFundValue(value);
		}
	}

	/**
	 * 更新该基金某一天的净值，同时检查是否有指令要执行
	 * @param value
	 */
	public void addFundValue(FundDayValue value) {
		this.fundValueMap.put(value.getToday(), value);
		checkOperationCmdsToHandle(value.getToday());
	}

	private void checkOperationCmdsToHandle(String currentDay) {
		Iterator<FundOperationCmd> iterator = operationCmds.iterator();
		while (iterator.hasNext()) {
			FundOperationCmd cmd = iterator.next();
			if (cmd.getToday().equals(currentDay)) {
				handleCmd(cmd);
				iterator.remove();
			}
		}
	}

	private void handleCmd(FundOperationCmd cmd) {
		FundDayValue todayFundValue = fundValueMap.get(cmd.getToday());
		if (null == todayFundValue) {
			logger.error("fundvalue should be exist");
			return;
		}
		if (cmd.getCmdType() == CmdType.ADD_AMOUNT) {
			double payAmount = cmd.getNum();
			double charge = payAmount * BUY_RATE;
			this.upToNowProfit -= charge;
			HoldingValue holding = HoldingValue.parseFromAmount(cmd.getToday(), payAmount - charge);
			holding.setValue(todayFundValue.getNetValue());
			this.holdingValues.add(holding);
		} else if (cmd.getCmdType() == CmdType.DEL_QUANTITY) {
			double delQuantity = cmd.getNum();
			Iterator<HoldingValue> iterator = holdingValues.iterator();
			while (iterator.hasNext()) {
				HoldingValue holding = iterator.next();
				if (!holding.isUpdate()) continue;
				
				if (holding.getQuantity() < delQuantity) {
					delQuantity -= holding.getQuantity();
					upToNowProfit += (todayFundValue.getNetValue() - holding.getValue()) * holding.getQuantity();
					iterator.remove();
				}
			}
		}
	}
}
