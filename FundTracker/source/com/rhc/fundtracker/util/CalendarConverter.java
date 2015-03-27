package com.rhc.fundtracker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarConverter {
	public static String getAnotherDay(String todayStr, int diff) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(todayStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + diff);
		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	public static String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());
		return format.format(curDate);
	}

	public static int convertDateToInt(String dateStr) {
		Calendar c = Calendar.getInstance();
		try {
			if (dateStr.indexOf('-') >= 0) {
				c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr));
			} else {
				c.setTime(new SimpleDateFormat("yyyy/MM/dd").parse(dateStr));
			}
			String convertedStr = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
			int result = Integer.valueOf(convertedStr);
			return result;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static int dayDiff(String date1, String date2) {
		return convertDateToInt(date1) - convertDateToInt(date2);
	}

	public static void main(String[] args) {
		return;
	}
}
