package com.rhc.fundtracker.fund;

import java.util.ArrayList;

import com.rhc.fundtracker.util.TextFileManager;

public class FundOperationCmd {
	private String today = null;
	private String fundId = null;
	private CmdType cmdType = null;
	private double num = 0.0;

	public static ArrayList<FundOperationCmd> parseFromFile(String cmdFile) {
		ArrayList<FundOperationCmd> res = new ArrayList<FundOperationCmd>();
		ArrayList<String> contents = TextFileManager.readFile(cmdFile);
		for (String line : contents) {
			String[] arr = line.split(" ");
			FundOperationCmd cmd = new FundOperationCmd();
			cmd.setToday(arr[0]);
			cmd.setFundId(arr[1]);
			cmd.setCmdType(CmdType.valueOf(arr[2]));
			cmd.setNum(Double.valueOf(arr[3]));
			res.add(cmd);
		}
		return res;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(fundId);
		return builder.toString();
	}

	public enum CmdType {
		ADD_AMOUNT, DEL_QUANTITY, RESET_AMOUNT
	}


	public double getNum() {
		return num;
	}

	public void setNum(double num) {
		this.num = num;
	}

	public String getFundId() {
		return fundId;
	}

	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	public CmdType getCmdType() {
		return cmdType;
	}

	public void setCmdType(CmdType cmdType) {
		this.cmdType = cmdType;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

}
