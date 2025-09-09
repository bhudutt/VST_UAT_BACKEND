/**
 * 
 */
package com.hitech.dms.web.model.common;

import java.util.Date;

import com.hitech.dms.utils.DateToStringParserUtils;

/**
 * @author Dinesh
 * @since  19-Aug-2021
 */
public class ServerDateTimeModel {

	public String serverDate;
	public String serverDateTime;
	
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

 
	 
}
