package com.guogee.smartwatch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

//import com.guogee.smartwatch.HealthyAccountHolder;
import com.guogee.smartwatch.R;

import android.content.Context;


public class DateTimeUtil {
	
	public static String getDateStringByDate(Date date){
		SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMdd");	
		String dateString =dateformat1.format(date);
		return dateString;
	}
	
	public static String getDateString(int pre){
		SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
		Date date = null;
		if(pre<0) calendar.add(Calendar.DATE, pre);
		String dateString =dateformat1.format(calendar.getTime());
		return dateString;
	}
	

	public static String getShareDateStringByDate(Date date,Context context){
		
		SimpleDateFormat dateformat1=null;		
			if(context.getResources().getConfiguration().locale.getLanguage().contains("zh")){
				dateformat1=new SimpleDateFormat("yyyy"+"年"+"MM"+"月"+"dd"+"日");
			}else{
				dateformat1=new SimpleDateFormat("dd MMMM yyyy");
			}
			String dateString =dateformat1.format(date);
		return dateString;
	}
	
	public static  String getMonDateString(int pre,Context context){
		SimpleDateFormat dateformat1=null;
		if(context.getResources().getConfiguration().locale.getLanguage().contains("zh")){
			dateformat1=new SimpleDateFormat("MM"+context.getString(R.string.str_current_mounth)+"dd"+context.getString(R.string.str_current_date));
		}else{
			dateformat1=new SimpleDateFormat("MMMM dd");
		}		
		Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
		Date date = null;
		if(pre<0) calendar.add(Calendar.DATE, pre);
		String dateString =dateformat1.format(calendar.getTime());
		return getWeek(calendar,context)+dateString;
	}
	
	public static Date getCurrentDate(int pre){
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		if(pre<0) calendar.add(Calendar.DATE, pre);
		return date;
	}
	
	
	public static String getDateStringByCalendar(int pre,Calendar calendar){
		SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		if(pre<0) calendar.add(Calendar.DATE, pre);
		String dateString =dateformat1.format(calendar.getTime());
		return dateString;
	}
	
	public static int getWhichOfMonthDateByDate(Date date){
		final SimpleDateFormat formatterDay = new SimpleDateFormat("dd");
		String dateString =formatterDay.format(date);
		return Integer.parseInt(dateString);
	}
	
	public static String getYearMothString(int year,int month){
		String yearMonth = String.valueOf(year);
		if(month<10)yearMonth+="0"+month;
		else{
			yearMonth+=month;
		}
		return yearMonth;
	}
	
	public static String getYearMothString(String year,String month){
		String yearMonth = year;
		if(month.length()<2)yearMonth+="0"+month;
		else{
			yearMonth+=month;
		}
		return yearMonth;
	}
	
	
	 private static String getWeek(Calendar c,Context context) {		  
		  String[] weekArray = context.getResources().getStringArray(R.array.weekStrings);
		  return weekArray[c.get(Calendar.DAY_OF_WEEK)-1];
		 
		 }
	 
//	 public static int[] getStartSyncTime(Context mContext){
//		 int[] yearMonthDay = new int[3];
//		 String lastSyncTime = HealthyAccountHolder.getLastSyncTime(mContext);
//		 yearMonthDay[0] = Integer.parseInt(lastSyncTime.substring(0, 4));
//		 yearMonthDay[1] = Integer.parseInt(lastSyncTime.substring(4, 6));
//		 yearMonthDay[2] = Integer.parseInt(lastSyncTime.substring(6, 8));
//		 DebugUtil.println( yearMonthDay[0]+"year"+ yearMonthDay[1]+"month"+ yearMonthDay[2]+"day");
//		 
//		 return yearMonthDay;
//	 }
	 
	 public static String getNowTime(){
			SimpleDateFormat timeformat =new SimpleDateFormat("yyyyMMddHHmmss");			
			String time = timeformat.format(new Date());
			return time;
		}
	 
	 

}
