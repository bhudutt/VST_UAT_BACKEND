package com.hitech.dms.app.common;

/**
 * Response status code
 */
public enum ResponseCode {

	// system module
	SUCCESS(0, "operation successful"), ERROR(1, "Operation failed"), SERVER_ERROR(500, "Server Exception"),

	// Generic module 1xxxx
	ILLEGAL_ARGUMENT(10000, "Argument is invalid"), REPETITIVE_OPERATION(10001, "Do not repeat operation"),
	ACCESS_LIMIT(10002, "The request is too frequent, please try again later"),
	MAIL_SEND_SUCCESS(10003, "Mail sent successfully"),
	MAIL_SEND_ERROR(10004, "Error While Sending Mail."),

	// User module 2xxxx
	NEED_LOGIN(20001, "Login failed"), USERNAME_OR_PASSWORD_EMPTY(20002, "Username or password cannot be empty"),
	USERNAME_OR_PASSWORD_WRONG(20003, "Incorrect username or password"), USER_NOT_EXISTS(20004, "User does not exist"),
	WRONG_PASSWORD(20005, "Wrong password"),

	// order module 4xxxx

	;

	ResponseCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private Integer code;

	private String msg;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}