package org.rhm.stock.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockUtil {
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static Date toMongoDate(Date inDate) throws ParseException {
		DateFormat df = new SimpleDateFormat(YYYY_MM_DD);
		String dateStr = df.format(inDate);
		return df.parse(dateStr);
	}
	
	public static Date stringToDate(String dateStr) throws ParseException {
		DateFormat df = new SimpleDateFormat(YYYY_MM_DD);
		Date dateObj = null;
		dateObj = df.parse(dateStr);
		return dateObj;
	}
	
	public static String dateToString(Date date) {
		DateFormat df = new SimpleDateFormat(YYYY_MM_DD);
		String dateStr = null;
		dateStr = df.format(date);
		return dateStr;
	}
}
