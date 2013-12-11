/**
 * 
 */
package com.android.aid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class StampHelper {
	
	private static final String TIME_ZONE_PRC		= "PRC";
	private static final String TIME_ZONE_UTC		= "UTC";
	private static final String STAMP_TO_STRING		= "yyyy-MM-dd HH:mm:ss.SSSZ";
	private static final String STAMP_TO_DATE		= "yyyy-MM-dd";
	private static final String STAMP_TO_DATETIME	= "yyyy-MM-dd HH:mm:ss";
	private static final String STAMP_TO_DATEHOUR	= "yyyyMMddHH";
	private static final String STAMP_TO_DATE_INT	= "yyyyMMdd";
	private static final String STAMP_TO_DATEMINUTE	= "yyyyMMddHHmm";
	
	
	public static String StampToString(long stamp){
/*
		String[] formats = new String[] {  
				"yyyy-MM-dd",   
				"yyyy-MM-dd HH:mm",  
				"yyyy-MM-dd HH:mmZ",   
				"yyyy-MM-dd HH:mm:ss.SSSZ",
				  "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
				}; 
*/		
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(stamp);
		date =  calendar.getTime();
		
    	String format = STAMP_TO_STRING; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_PRC));	
		
		return sdf.format(date);
		
    }
	
	public static String StampToDate(long stamp){
		return StampToString(stamp).substring(0, STAMP_TO_DATE.length());
	}
	
	
	public static String StampToDateTime(long stamp){
		return StampToString(stamp).substring(0, STAMP_TO_DATETIME.length());
	}
	
	public static int StampToDateHour(long stamp){
		int datehour = 0;
		
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(stamp);
		date =  calendar.getTime();
		
    	String format = STAMP_TO_DATEHOUR; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_PRC));	
		
		try{
			datehour = Integer.parseInt(sdf.format(date));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return datehour;
	}

	public static int StampToDateMinute(long stamp){
		int dateminute = 0;
		
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(stamp);
		date =  calendar.getTime();
		
    	String format = STAMP_TO_DATEMINUTE; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_PRC));	
		
		try{
			dateminute = Integer.parseInt(sdf.format(date).substring(2));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return dateminute;
	}
	
	public static int StampToDateInt(long stamp){
		int date = 0;
		
		Date datetime = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(stamp);
		datetime =  calendar.getTime();
		
    	String format = STAMP_TO_DATE_INT; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_PRC));	
		
		try{
			date = Integer.parseInt(sdf.format(datetime));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//Log.i("StampHelper", "date_int:"+date);
		return date;
	}

	public static String getYesterday(){
		return StampToDate(System.currentTimeMillis()-getDateStamp());
	}
	
	public static String getToday(){
		return StampToDate(System.currentTimeMillis());
	}
	
	public static long getSecondStamp(){
		long second = 1000;
		return second;
	}
	
	public static long getMinuteStamp(){
		long minute = getSecondStamp() * (long)(60);
		return minute;
	}
	
	public static long getHourStamp(){
		long hour = getMinuteStamp() * (long)(60);
		return hour;
	}
	
	public static long getDateStamp(){
		long date = getHourStamp() * (long)(24);
		return date;
	}
	
	public static long getMonthStamp(){
		long month = getDateStamp() * (long)(30);
		return month;
	}
	
	public static long getTrackerStamp(){
		return getMinuteStamp();
//		return getSecondStamp()*5;
	}
	
	public static long getTrackerSleepStamp(){
		return getMinuteStamp();
//		return getSecondStamp()*5;
	}

}
