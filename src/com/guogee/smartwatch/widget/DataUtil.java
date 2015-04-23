package com.guogee.smartwatch.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class DataUtil {
	private static String mYear;
	private static String mMonth;
	private static String mDay;
	private static String mWay;

	public String StringData() {
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mWay)) {
			mWay = "天";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		if(mMonth.length() == 1)
		{
			mMonth= "0"+mMonth;
		}
		if(mDay.length() == 1)
		{
			mDay= "0"+mDay;
		}
		return mYear + "年" + mMonth + "月" + mDay + "日" + " " + "星期" + mWay;
	}

	
	public String StrDataNoWeek() {
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		if(mMonth.length() == 1)
		{
			mMonth= "0"+mMonth;
		}
		if(mDay.length() == 1)
		{
			mDay= "0"+mDay;
		}
		return mYear + "-" + mMonth + "-" + mDay;
	}
	
	
	public String StringDataNoWeek() {
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		if(mMonth.length() == 1)
		{
			mMonth= "0"+mMonth;
		}
		if(mDay.length() == 1)
		{
			mDay= "0"+mDay;
		}
//		return mYear + "年" + mMonth + "月" + mDay + "日";
		
		return mYear + "/" + mMonth + "/" + mDay ;
	}

	/*
	 * 获取指定日后 后 dayAddNum 天的 日期
	 * 
	 * @param day 日期，格式为String："2013-9-3";
	 * 
	 * @param dayAddNum 增加天数 格式为int;
	 * 
	 * @return
	 */
	public static String getDateStr(String day, int dayAddNum) {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy" + "年" + "MM" + "月"
//				+ "dd" + "日");
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");    
//		String date=sdf.format(new java.util.Date());
//		timeData.setTypeface(Util.DateTitleFonts(getActivity(), timeData));

		
		Date nowDate = new Date();
		try {
			nowDate = df.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60
				* 1000);
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy" + "年"
//				+ "MM" + "月" + "dd" + "日");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		
		String dateOk = simpleDateFormat.format(newDate2);
		return dateOk;
	}
	
	public static String getDateString(String day, int dayAddNum) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy" + "-" + "MM" + "-"
				+ "dd" );
		Date nowDate = new Date();
		try {
			nowDate = df.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60
				* 1000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy" + "-"
				+ "MM" + "-" + "dd");
		String dateOk = simpleDateFormat.format(newDate2);
		return dateOk;
	}

	
	/*
	 * 获取指定日后 后 dayAddNum 天的 日期
	 * 
	 * @param day 日期，格式为String："2013-9-3";
	 * 
	 * @param dayAddNum 增加天数 格式为int;
	 * 
	 * @return
	 */
	public static String getDateStrYHD(String day, int dayAddNum) {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		
		Date nowDate = new Date();
		try {
			nowDate = df.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60
				* 1000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateOk = simpleDateFormat.format(newDate2);
		return dateOk;
	}
	
	public static String getSelectDate(String day) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		Date nowDate = new Date();
		try {
			nowDate = df.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date newDate2 = new Date(nowDate.getTime());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateOk = simpleDateFormat.format(newDate2);
		return dateOk;
	}
	
	// 根据日期取得星期几
		public String getWeeks(String pTime) {

			String Week = "";

			SimpleDateFormat format = new SimpleDateFormat("yyyy" + "-" + "MM" + "-"
					+ "dd");
			Calendar c = Calendar.getInstance();
			try {

				c.setTime(format.parse(pTime));

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 1) {
				Week += "天";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 2) {
				Week += "一";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 3) {
				Week += "二";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 4) {
				Week += "三";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 5) {
				Week += "四";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 6) {
				Week += "五";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 7) {
				Week += "六";
			}

			return pTime + " " + "星期" + Week;
		}
	
	
	// 根据日期取得星期几
	public String getWeek(String pTime) {

		String Week = "";
//		SimpleDateFormat format = new SimpleDateFormat("yyyy" + "年" + "MM" + "月"
//				+ "dd" + "日");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}

//		return pTime + " " + "星期" + Week;
		
		return pTime ;
	}
	/***
	 * 加减日期
	 * @param date
	 * @param addDay
	 * @return
	 */
	public static Date  dateAddDay(Date date,int addDay){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, addDay);
		return c.getTime();
	}
	/***
	 * 获得月份
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH)+1;
		
	}
	/***
	 * 获得日期
	 * @param date
	 * @return
	 */
	public static int getDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DATE);
		
	}
	/***
	 * 获得一年中的第几周
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
		
	}
	
}

