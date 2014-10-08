package de.craut.util;

import java.util.Date;

public class AdvancedDateFormat {

	public static String day(Date date) {
		Date today = new Date();
		long diff = (today.getTime() - date.getTime()) / (86400000);
		return diff == 1 ? "YESTERDAY" : diff > 1 ? String.valueOf(diff) + " " + "DAYS AGO" : "TODAY";
	}

	public static String day(long time) {
		return day(new Date(time));
	}

}
