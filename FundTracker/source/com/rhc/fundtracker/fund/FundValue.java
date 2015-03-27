package com.rhc.fundtracker.fund;

public class FundValue {
	private String date = null; // 对应的时间
	private double netValue = 0.0; // 净值
	private double aggregateValue = 0.0; // 累计值
	private double growthRate = 0.0; // 相比前一天的增长率

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(date).append('\t');
		builder.append(netValue).append('\t');
		builder.append(aggregateValue).append('\t');
		builder.append(growthRate);
		return builder.toString();
	}

	public static FundValue parse(String line) {
		String[] fields = line.split("\t");
		FundValue value = new FundValue();
		try {
			value.date = fields[0];
			value.netValue = Double.valueOf(fields[1]);
			value.aggregateValue = Double.valueOf(fields[2]);
			value.growthRate = Double.valueOf(fields[3]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getNetValue() {
		return netValue;
	}

	public void setNetValue(double netValue) {
		this.netValue = netValue;
	}

	public void setNetValue(String netValue) {
		this.netValue = Double.valueOf(netValue);
	}

	public double getAggregateValue() {
		return aggregateValue;
	}

	public void setAggregateValue(double aggregateValue) {
		this.aggregateValue = aggregateValue;
	}

	public void setAggregateValue(String aggregateValue) {
		this.aggregateValue = Double.valueOf(aggregateValue);
	}

	public double getGrowthRate() {
		return growthRate;
	}

	public void setGrowthRate(double growthRate) {
		this.growthRate = growthRate;
	}

	public void setGrowthRate(String growthRate) {
		if (growthRate.lastIndexOf('%') >= 0) {
			growthRate = growthRate.substring(0, growthRate.length() - 1);
		}
		this.growthRate = Double.valueOf(growthRate);
	}
}