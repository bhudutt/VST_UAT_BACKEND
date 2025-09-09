/**
 * 
 */
package com.hitech.dms.app.utils;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author dinesh.jakhar
 *
 */
public class DateToStringParserUtils {
	/**
	 * Date to String parser
	 * 
	 * @param date
	 * @return parseValue
	 */
	public static String parseDateToString(Date date) {
		SimpleDateFormat dateFormatter = null;
		String parseValue = null;
		if (null != date) {
			dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
			parseValue = dateFormatter.format(date);
		}
		return parseValue;
	}
	public static String parseDateToStringDDMMYYYY(Date date) {
		SimpleDateFormat dateFormatter = null;
		String parseValue = null;
		if (null != date) {
			dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
			parseValue = dateFormatter.format(date);
		}
		return parseValue;
	}
	
	public static String parseDateToStringYYYYMMDD(Date date) {
		SimpleDateFormat dateFormatter = null;
		String parseValue = null;
		if (null != date) {
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			parseValue = dateFormatter.format(date);
		}
		return parseValue;
	}
	
	public static String parseDateToStringddMMMyyyy(Date date) {
		SimpleDateFormat dateFormatter = null;
		String parseValue = null;
		if (null != date) {
			dateFormatter = new SimpleDateFormat("dd MMM yyyy");
			parseValue = dateFormatter.format(date);
		}
		return parseValue;
	}
	
	/**
	 * Date to String parser
	 * 
	 * @param date
	 * @return parseValue
	 */
	public static String parseDateToSQLFormat(Date date) {
		SimpleDateFormat dateFormatter = null;
		String parseValue = null;
		if (null != date) {
			dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
			parseValue = dateFormatter.format(date);
		}
		return parseValue;
	}

	/**
	 * 
	 * @param date
	 * @return parseValue
	 */
	public static String parseDateTimeToString(Date date) {
		SimpleDateFormat dateFormatter = null;
		String parseValue = null;
		if (null != date) {
			dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			parseValue = dateFormatter.format(date);
		}
		return parseValue;
	}

	/**
	 * 
	 * @param date
	 * @return formattedDate
	 */
	public static String parseDateToNumericFormat(Date date) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String formattedDate = null;
		try {
			date = ((DateFormat) formatter).parse("2011-04-13");
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			formattedDate = formatter.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formattedDate;
	}

	public static String getServerDateTime() {
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	    f.setTimeZone(TimeZone.getTimeZone("UTC"));
	    Date date=new Date();

	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.HOUR_OF_DAY, 5);
	    cal.add(Calendar.MINUTE, 30);
	    
	    return  f.format(cal.getTime());
	}
	
	public static long parseDateToSQLDate(String date) {
		SimpleDateFormat dateFormatter = null;
		long parseValue = 0;
		if (null != date) {
			dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
			try {
				parseValue = dateFormatter.parse(date).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return parseValue;
	}
	
	/**
	 * setting end time of the day i.e Fri Apr 12 23:59:59 IST 2019
	 */
	public static Date addEndTimeOFTheDay(Date date)  {
		if(date!=null){
			
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String dateformate = dateFormat1.format(date);
			dateformate = dateformate+" 23:59";
			dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(date);
//			calendar.set(Calendar.MILLISECOND, 999);
//	        calendar.set(Calendar.SECOND, 59);
//	        calendar.set(Calendar.MINUTE,59);
//	        calendar.set(Calendar.HOUR, 23);
			
	        try {
				date = dateFormat1.parse(dateformate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				date  = null;
			}
		}
		return date;

	}
	/**
	 * setting strat time of the day i.e Fri Apr 12 12:00:00 IST 2019
	 */
	
	public static Date addStratTimeOFTheDay(Date date)  {
		if(date!=null){
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(date);
//			calendar.set(Calendar.MILLISECOND, 0);
//	        calendar.set(Calendar.SECOND, 0);
//	        calendar.set(Calendar.MINUTE,0);
//	        calendar.set(Calendar.HOUR, 0);
//	        date = calendar.getTime();
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String dateformate = dateFormat1.format(date);
			dateformate = dateformate+" 00:00";
			dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			 try {
					date = dateFormat1.parse(dateformate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					date  = null;
				}
		}
		return date;
	}
	
	public static String parseMonthAndYearToString(Date date) {
		SimpleDateFormat dateFormatter = null;
		String parseValue = null;
		if (null != date) {
			dateFormatter = new SimpleDateFormat("MM/yyyy");
			parseValue = dateFormatter.format(date);
		}
		return parseValue;
	}
	
	static public Boolean compareDateBet2DatesWithDays(Date date1, Date date2, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -days);
		if (date1 != null && date2 != null) {
			if (date1.before(date2) || cal.getTime().after(date2)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	 
	
}
