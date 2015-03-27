package com.rhc.fundtracker.fund;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FundOperationCmd {
	private String date = null;
	private String fundId = null;
	private CmdType cmdType = null;
	private double num = 0.0;
	
	public static ArrayList<FundOperationCmd> parseFromFile(String cmdFile) {
		ArrayList<FundOperationCmd> res = new ArrayList<FundOperationCmd>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(cmdFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] arr = line.split(" ");
				FundOperationCmd cmd = new FundOperationCmd();
				cmd.setDate(arr[0]);
				cmd.setFundId(arr[1]);
				cmd.setCmdType(CmdType.valueOf(arr[2]));
				cmd.setNum(Double.valueOf(arr[3]));
				res.add(cmd);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

}
