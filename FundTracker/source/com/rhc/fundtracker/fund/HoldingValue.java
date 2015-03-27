package com.rhc.fundtracker.fund;

public class HoldingValue {
	private double quantity = 0.0; // 持有的份额数
	private double value = 0.0; // 对应的净值
	private double amount = 0.0; // =quantity*value
	private String date = null;
	private boolean isUpdate = false;

	public static HoldingValue parseFromAmount(String date, double amount) {
		HoldingValue value = new HoldingValue();
		value.amount = amount;
		value.date = date;
		return value;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
		this.quantity = this.amount / value;
		isUpdate = true;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	
}
