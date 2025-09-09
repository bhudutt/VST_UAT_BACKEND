package com.hitech.dms.app.utils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class DmsCollectionUtils {



	/**
	 * To check for empty
	 * 
	 * @param t
	 * @return
	 */
	public static boolean isNullOrEmpty(Collection<? extends Object> t) {
		if (null == t) {
			return true;
		}
		if (null != t && t.size() == 0) {
			return true;
		}
		return false;

	}
	

	/**
	 * To check for not empty
	 * 
	 * @param t
	 * @return
	 */
	public static boolean isNotEmpty(Collection<? extends Object> t) {
		if (null != t && t.size() != 0) {
			return true;
		}
		return false;

	}
	
	/**
	 * This method is used to convert java.util.Map to JSON Map
	 * 
	 * @param java.util.Map
	 * @return
	 */
	public static String getJsonMap(Map<String, Object> map){
		StringBuilder sb = new StringBuilder("{");
		for(Map.Entry<String, Object> entry : map.entrySet()){
			sb.append("\""+entry.getKey()+"\":\""+entry.getValue()+"\",");
		}
		String jsonMap = sb.substring(0,sb.lastIndexOf(","))+"}";
		return jsonMap;
	}
	
	/**
	 * This method is used to convert String date format to YYYYMMDD
	 * 
	 * @param java.util.Map
	 * @return
	 */
	public static String getDateYYYYMMDD(String date){
		String sDate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
			Date fdate = formatter.parse(date);
			sDate = formatter1.format(fdate);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sDate;
	}
	
	public static String getDateDDMMYYYY(String date){
		String sDate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter1 = new SimpleDateFormat("ddMMyyyy");
			Date fdate = formatter.parse(date);
			sDate = formatter1.format(fdate);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sDate;
	}
	public static String getYYYYMMDD(String date){
		String sDate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
			Date fdate = formatter.parse(date);
			sDate = formatter1.format(fdate);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sDate;
	}
	
	public static String getYYYYMMDDFOR(String date){
		String sDate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
			Date fdate = formatter.parse(date);
			sDate = formatter1.format(fdate);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sDate;
	}
	

}
