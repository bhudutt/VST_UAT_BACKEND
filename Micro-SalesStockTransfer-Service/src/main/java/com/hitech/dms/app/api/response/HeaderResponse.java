/**
 * 
 */
package com.hitech.dms.app.api.response;

/**
 * @author dinesh.jakhar
 *
 */
public class HeaderResponse {
	private MessageCodeResponse responseCode;
	private Object responseData;

	public MessageCodeResponse getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(MessageCodeResponse responseCode) {
		this.responseCode = responseCode;
	}

	public Object getResponseData() {
		return responseData;
	}

	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}
}
