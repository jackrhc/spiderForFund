package model;

import java.util.ArrayList;
import java.util.List;

public class FundValue {
	private List<ValuePair> valueList = null;
	
	public FundValue() {
		valueList = new ArrayList<FundValue.ValuePair>();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		return builder.toString();
	}

	
	private class ValuePair {
		private String date = null; // 对应的时间
		private double netValue = 0.0; // 净值
		private double aggregateValue = 0.0; // 累计值
	}
}