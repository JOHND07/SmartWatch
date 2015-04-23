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
		mYear = String.valueOf(c.get(Calendar.YEAR)); // ��ȡ��ǰ���
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// ��ȡ��ǰ�·�
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// ��ȡ��ǰ�·ݵ����ں���
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mWay)) {
			mWay = "��";
		} else if ("2".equals(mWay)) {
			mWay = "һ";
		} else if ("3".equals(mWay)) {
			mWay = "��";
		} else if ("4".equals(mWay)) {
			mWay = "��";
		} else if ("5".equals(mWay)) {
			mWay = "��";
		} else if ("6".equals(mWay)) {
			mWay = "��";
		} else if ("7".equals(mWay)) {
			mWay = "��";
		}
		if(mMonth.length() == 1)
		{
			mMonth= "0"+mMonth;
		}
		if(mDay.length() == 1)
		{
			mDay= "0"+mDay;
		}
		return mYear + "��" + mMonth + "��" + mDay + "��" + " " + "����" + mWay;
	}

	
	public String StrDataNoWeek() {
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // ��ȡ��ǰ���
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// ��ȡ��ǰ�·�
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// ��ȡ��ǰ�·ݵ����ں���
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
		mYear = String.valueOf(c.get(Calendar.YEAR)); // ��ȡ��ǰ���
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// ��ȡ��ǰ�·�
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// ��ȡ��ǰ�·ݵ����ں���
		if(mMonth.length() == 1)
		{
			mMonth= "0"+mMonth;
		}
		if(mDay.length() == 1)
		{
			mDay= "0"+mDay;
		}
//		return mYear + "��" + mMonth + "��" + mDay + "��";
		
		return mYear + "/" + mMonth + "/" + mDay ;
	}

	/*
	 * ��ȡָ���պ� �� dayAddNum ��� ����
	 * 
	 * @param day ���ڣ���ʽΪString��"2013-9-3";
	 * 
	 * @param dayAddNum �������� ��ʽΪint;
	 * 
	 * @return
	 */
	public static String getDateStr(String day, int dayAddNum) {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy" + "��" + "MM" + "��"
//				+ "dd" + "��");
		
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
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy" + "��"
//				+ "MM" + "��" + "dd" + "��");
		
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
	 * ��ȡָ���պ� �� dayAddNum ��� ����
	 * 
	 * @param day ���ڣ���ʽΪString��"2013-9-3";
	 * 
	 * @param dayAddNum �������� ��ʽΪint;
	 * 
	 * @return
	 */
	public static String getDateStrYHD(String day, int dayAddNum) {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy��MM��dd��");
		
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
		SimpleDateFormat df = new SimpleDateFormat("yyyy��MM��dd��");
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
	
	// ��������ȡ�����ڼ�
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
				Week += "��";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 2) {
				Week += "һ";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 3) {
				Week += "��";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 4) {
				Week += "��";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 5) {
				Week += "��";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 6) {
				Week += "��";
			}
			if (c.get(Calendar.DAY_OF_WEEK) == 7) {
				Week += "��";
			}

			return pTime + " " + "����" + Week;
		}
	
	
	// ��������ȡ�����ڼ�
	public String getWeek(String pTime) {

		String Week = "";
//		SimpleDateFormat format = new SimpleDateFormat("yyyy" + "��" + "MM" + "��"
//				+ "dd" + "��");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "��";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "һ";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "��";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "��";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "��";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "��";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "��";
		}

//		return pTime + " " + "����" + Week;
		
		return pTime ;
	}
	/***
	 * �Ӽ�����
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
	 * ����·�
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH)+1;
		
	}
	/***
	 * �������
	 * @param date
	 * @return
	 */
	public static int getDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DATE);
		
	}
	/***
	 * ���һ���еĵڼ���
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
		
	}
	
}

