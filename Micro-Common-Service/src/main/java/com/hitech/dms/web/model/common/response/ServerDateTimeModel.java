/**
 * 
 */
package com.hitech.dms.web.model.common.response;

import java.util.Date;
import java.util.Map;

import com.hitech.dms.app.utils.DateToStringParserUtils;

/**
 * @author dinesh.jakhar
 *
 */
public class ServerDateTimeModel {
	private String serverDate;
	private String serverDateTime;
	private Map<String, Object> mapData;
	
	public ServerDateTimeModel(){
		serverDateTime = DateToStringParserUtils.getServerDateTime();
		serverDate = DateToStringParserUtils.parseDateToString(new Date());
	}

	/**
	 * @return the serverDate
	 */
	public String getServerDate() {
		if(serverDate == null || serverDate.equals("")) {
			serverDate = DateToStringParserUtils.parseDateToString(new Date());
		}
		return serverDate;
	}

	/**
	 * @param serverDate the serverDate to set
	 */
	public void setServerDate(String serverDate) {
		if(serverDate == null || serverDate.equals("")) {
			serverDate = DateToStringParserUtils.parseDateToString(new Date());
		}
		this.serverDate = serverDate;
	}

	/**
	 * @return the serverDateTime
	 */
	public String getServerDateTime() {
		if(serverDateTime == null || serverDateTime.equals("")) {
			serverDateTime = DateToStringParserUtils.getServerDateTime();
		}
		return serverDateTime;
	}

	/**
	 * @param serverDateTime the serverDateTime to set
	 */
	public void setServerDateTime(String serverDateTime) {
		if(serverDateTime == null || serverDateTime.equals("")) {
			serverDateTime = DateToStringParserUtils.getServerDateTime();
		}
		this.serverDateTime = serverDateTime;
	}

	public Map<String, Object> getMapData() {
		if(mapData == null) {
			mapData = DateToStringParserUtils.getDayMonthYear();
		}
		return mapData;
	}

	public void setMapData(Map<String, Object> mapData) {
		if(mapData == null) {
			mapData = DateToStringParserUtils.getDayMonthYear();
		}
		this.mapData = mapData;
	}
	
}
