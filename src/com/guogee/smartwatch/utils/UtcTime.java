package com.guogee.smartwatch.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtcTime {

	public static int UTCTIME_BEGYEAR = 2000; // UTC started at 00:00:00 January
												// 1, 2000
	public static int UTCIME_DAY = 86400; // 24 hours * 60 minutes * 60 seconds

	public int seconds;
	public int minutes;
	public int hour;
	public int day;
	public int month;
	public int year;
	public Date date;
	public static final String[] week = { "", "日", "一", "二", "三", "四", "五", "六" };

	public UtcTime() {

		Calendar now = Calendar.getInstance();
		date = now.getTime();
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH) + 1;
		day = now.get(Calendar.DAY_OF_MONTH);
		hour = now.get(Calendar.HOUR_OF_DAY);
		minutes = now.get(Calendar.MINUTE);
		seconds = now.get(Calendar.SECOND);
	}

	public UtcTime(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH) + 1;
		day = now.get(Calendar.DAY_OF_MONTH);
		hour = now.get(Calendar.HOUR_OF_DAY);
		minutes = now.get(Calendar.MINUTE);
		seconds = now.get(Calendar.SECOND);
		this.date = date;
	}

	public Date getDate() {
		Calendar now = Calendar.getInstance();
		now.set(year, month - 1, day, hour, minutes, seconds);
		return now.getTime();
	}

	/***
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public String getStrDateyyyyMMddHms() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(getDate());
	}

	/***
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public String getStrDateyMdHWeek() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Calendar now = Calendar.getInstance();
		Date d = getDate();
		now.setTime(d);

		return sdf.format(d) + " 星期" + week[now.get(Calendar.DAY_OF_WEEK)];
	}

	/***
	 * yyyy-MM-dd
	 * 
	 * @return
	 */
	public String getStrDateyyyyMMdd() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(getDate());
	}

	public static String setDate(int year, int month, int day) {
		String date;
		if (day < 10 && month >= 10) {
			date = year + "-" + month + "-" + "0" + day;
		} else if (day < 10 && month < 10) {
			date = year + "-" + "0" + month + "-" + "0" + day;
		} else if (day >= 10 && month < 10) {
			date = year + "-" + "0" + month + "-" + day;
		} else {
			date = year + "-" + month + "-" + day;
		}
		return date;
	}

	public static String getTime(Long time) {
		StringBuilder strTime = new StringBuilder();// timeStr=(mtimelen/3600)+":"+(mtimelen%3600/60)+":"+(mtimelen%60);//计算时分秒
		long h = time / 3600;
		long min = time % 3600 / 60;
		long sec = time % 60;
		if (h < 10)
			strTime.append("0" + h +":");
		else
			strTime.append(h +":");
		if (min < 10)
			strTime.append("0" + min +":");
		else
			strTime.append(min +":");
		if (sec < 10)
			strTime.append("0" + sec);
		else
			strTime.append(sec);
		return strTime.toString();
	}
	
	public static String getMinTime(Long time) {
		StringBuilder strTime = new StringBuilder();// timeStr=(mtimelen/3600)+":"+(mtimelen%3600/60)+":"+(mtimelen%60);//计算时分秒
		long h = time / 3600;
		long min = time % 3600 / 60;
		if (h < 10)
			strTime.append("0" + h +":");
		else
			strTime.append(h +":");
		if (min < 10)
			strTime.append("0" + min);
		else
			strTime.append(min);
		return strTime.toString();
	}
	/*public static String getNoHTime(Long time) {
		StringBuilder strTime = new StringBuilder();// timeStr=(mtimelen/3600)+":"+(mtimelen%3600/60)+":"+(mtimelen%60);//计算时分秒
		long min = time % 3600 / 60;
		long sec = time % 60;
		if (min < 10)
			strTime.append("0" + min +":");
		else
			strTime.append(min +":");
		if (sec < 10)
			strTime.append("0" + sec);
		else
			strTime.append(sec);
		return strTime.toString();
	}*/
}
