package com.guogee.smartwatch.utils;

import java.util.Date;

public class ConvertUTC {
private final static long TIMELONG2000= 946656000;
	public static long ConvertUTCSecs(UtcTime utctime)
	{
		
		return utctime.getDate().getTime()/1000-TIMELONG2000;
	}
	
	public static UtcTime ConvertUTCTime(long secTime)
	{
		
		// calculate the time less than a day - hours, minutes, seconds
		UtcTime time = new UtcTime(new Date((secTime+TIMELONG2000)*1000));
//		  {
//		    long day = secTime % UtcTime.UTCIME_DAY;
//		    time.seconds = (int) (day % 60);
//		    time.minutes = (int) ((day % 3600) / 60);
//		    time.hour = (int) (day / 3600);
//		  }
//
//		  // Fill in the calendar - day, month, year
//		  {
//		    long numDays = secTime / UtcTime.UTCIME_DAY;
//		    time.year = UtcTime.UTCTIME_BEGYEAR;
//		    while ( numDays >= YearLength(time.year) )
//		    {
//		      numDays -= YearLength( time.year );
//		      time.year++;
//		    }
//
//		    time.month = 0;
//		    while ( numDays >= monthLength( IsLeapYear( time.year ), time.month ) )
//		    {
//		      numDays -= monthLength( IsLeapYear( time.year ), time.month );
//		      time.month++;
//		    }
//
//		    time.day = (int) numDays;
//		  }  
		   
		return time;
		
	}
}
