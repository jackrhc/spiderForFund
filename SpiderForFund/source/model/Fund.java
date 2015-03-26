package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import model.FundOperationCmd.CmdType;

public class Fund implements Cloneable {
	private String id = "";
	private String name = "";

	private double quantity = 0.0; // 最新持有的份额数
	private double todayWorth = 0.0; // 今天的基金价值
	private double yesterdayWorth = 0.0; // 昨天的基金价值
	private double upToNowProfit = 0.0; // 今天为止该基金的收益
	private String updateDate = null; // 最近一次更新对应日期
	private Map<String, FundValue> fundValueMap = null; // 时间到基金净值的映射
	private ArrayList<FundOperationCmd> operationCmds = null; // 待操作的序列
	private ArrayList<FundOperationCmd> historyCmds = null; // 历史操作序列
	private final double BUY_RATE = 0.006; // 申购费率（暂时不考虑不同平台不一样的情况）
	private String startDate = "2015-01-01";
	private String endDate = null;
	private final double SELL_RATE = 0.005; // 赎回费率

	public Fund() {
		init();
	}

	public Fund(String id, String name) {
		this.id = id;
		this.name = name;
		init();
	}

	private void init() {
		if (null == fundValueMap) {
			fundValueMap = new HashMap<String, FundValue>();
		}
		if (null == operationCmds) {
			operationCmds = new ArrayList<FundOperationCmd>();
		}
		if (null == historyCmds) {
			historyCmds = new ArrayList<FundOperationCmd>();
		}
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

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public boolean updateFundValue(String date, double netValue, double aggregateValue) {
		FundValue value = fundValueMap.get(date);
		if (null == value) {
			value = new FundValue(date, netValue, aggregateValue);
			fundValueMap.put(date, value);
			updateDate = date;
			yesterdayWorth = todayWorth;
			todayWorth = value.getNetValue() * quantity;
			upToNowProfit += (todayWorth - yesterdayWorth);
			handleOperationCmd();
			return true;
		}
		return false;
	}

	public void addOperationCmd(FundOperationCmd cmd) {
		if (null == operationCmds || operationCmds.isEmpty()) {
			operationCmds = new ArrayList<FundOperationCmd>();
		}
		operationCmds.add(cmd);
	}

	private void handleOperationCmd() {
		Iterator<FundOperationCmd> iterator = operationCmds.iterator();
		while (iterator.hasNext()) {
			FundOperationCmd cmd = iterator.next();
			FundValue value = fundValueMap.get(cmd.getDate());
			if (null == value) continue; // 对应的基金净值还没更新

			if (cmd.getCmdType() == CmdType.ADD_AMOUNT) {
				double loss = cmd.getNum() * BUY_RATE;
				double realAmount = cmd.getNum() - loss;
				quantity += realAmount / value.getNetValue();
				todayWorth += realAmount;
				upToNowProfit -= loss;
			} else if (cmd.getCmdType() == CmdType.DEL_QUANTITY) {
				double loss = (value.getNetValue() * cmd.getNum()) * SELL_RATE;
				todayWorth -= (value.getNetValue() * cmd.getNum());
				quantity -= cmd.getNum();
				upToNowProfit -= loss;
			} else if (cmd.getCmdType() == CmdType.RESET_AMOUNT) {
				upToNowProfit += (cmd.getNum() - todayWorth);
				todayWorth = cmd.getNum();
				quantity = todayWorth / value.getNetValue();
			}
			historyCmds.add(cmd);
			iterator.remove();
		}
	}

	public static String getHeaderStr() {
		return "name\t\ttodayWorth\tyestdayWorth\tupToNowProfit\tquantity\tupdateData\tvalue\t\tcmd\t\t";
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append('\t');
		builder.append((int) todayWorth).append("\t\t");
		builder.append((int) yesterdayWorth).append("\t\t");
		builder.append((int) upToNowProfit).append("\t\t");
		builder.append((int) quantity).append("\t\t");
		builder.append(updateDate).append("\t\t");
		for (FundOperationCmd cmd : operationCmds) {
			builder.append(cmd).append(',');
		}
		return builder.toString();
	}

	public ArrayList<FundOperationCmd> getOperationCmds() {
		return operationCmds;
	}

	public double getUpToNowProfit() {
		return upToNowProfit;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public double getYesterdayWorth() {
		return yesterdayWorth;
	}

	public void setYesterdayWorth(double yesterdayWorth) {
		this.yesterdayWorth = yesterdayWorth;
	}

	public double getTodayWorth() {
		return todayWorth;
	}

	public void setTodayWorth(double totayWorth) {
		this.todayWorth = totayWorth;
	}

	public Fund Clone() {
		Fund value = null;
		try {
			value = (Fund) super.clone();
			value.init();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return value;
	}

}
