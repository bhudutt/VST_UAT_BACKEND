package com.hitech.dms.app.api.response;

public class MessageCodeResponse extends ModelBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5429130577200827415L;
	private String code;
	private String message;
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
