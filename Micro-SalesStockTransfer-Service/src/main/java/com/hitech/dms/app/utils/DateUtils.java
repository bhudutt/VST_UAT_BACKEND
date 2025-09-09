/**
 * 
 */
package com.hitech.dms.app.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author dinesh.jakhar
 *
 */
public class DateUtils {

	public static boolean validateStartDateIsBefore(Date startDate, Date endDate) {
		Date sDate = getZeroTimeDate(startDate);
		Date eDate = getZeroTimeDate(endDate);

		if (sDate.before(eDate)) {
			return true;
		}

		if (sDate.after(eDate)) {
			return false;
		}

		return false;
	}
	
	public static boolean validateStartDateIsAfter(Date startDate, Date endDate) {
		Date sDate = getZeroTimeDate(startDate);
		Date eDate = getZeroTimeDate(endDate);

		if (sDate.before(eDate)) {
			return false;
		}

		if (sDate.after(eDate)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean validateStartDateIsAfterOrEqual(Date startDate, Date endDate) {
		Date sDate = getZeroTimeDate(startDate);
		Date eDate = getZeroTimeDate(endDate);

		if (sDate.before(eDate)) {
			System.out.println("Start date [" + startDate + "] is before end date [" + endDate + "]");
			return false;
		}

		if (sDate.after(eDate)) {
			System.out.println("Start date [" + endDate + "] is after end date [" + endDate + "]");
			return true;
		}else {
			System.out.println("Start date [" + startDate + "] and end date [" + endDate + "] are equal");
			return true;
		}
	}

	public static Date getZeroTimeDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		return date;
	}
	
	public static Date getNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        } else {
            calendar.roll(Calendar.MONTH, true);
        }
        return calendar.getTime();
    }
}
