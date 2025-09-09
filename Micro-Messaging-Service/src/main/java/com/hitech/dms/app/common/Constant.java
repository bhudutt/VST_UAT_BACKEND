package com.hitech.dms.app.common;

public class Constant {

	public static final int MAX_SIZE_PER_TIME = 1000;
	public static final int INDEX_ZERO = 0;
	public static final int INDEX_ONE = 1;
	public static final int INDEX_TWO = 2;
	public static final int INDEX_THREE = 3;

	public static final int NUMBER_ZERO = 0;
	public static final int NUMBER_ONE = 1;

	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String DOUBLE_STRIGULA = "--";
	public static final String REPLACEMENT_TARGET = "-99999%";

	public static final String UNKNOWN_TYPE = "Unknown type";

	public interface Redis {
		String OK = "OK";
		// expiration time, 60s, one minute
		Integer EXPIRE_TIME_MINUTE = 60;
		// expiration time, one hour
		Integer EXPIRE_TIME_HOUR = 60 * 60;
		// expiration time, one day
		Integer EXPIRE_TIME_DAY = 60 * 60 * 24;
		String TOKEN_PREFIX = "token:";
		String MSG_CONSUMER_PREFIX = "consumer:";
		String ACCESS_LIMIT_PREFIX = "accessLimit:";
		String FUND_RANK = "fundRank";
		String FUND_LIST = "fundList";
	}

	public interface LogType {
		// Log in
		Integer LOGIN = 1;
		// Sign out
		Integer LOGOUT = 2;
	}

	public interface MsgLogStatus {
		// message delivery
		Integer DELIVERING = 0;
		// Successful delivery
		Integer DELIVER_SUCCESS = 1;
		// delivery failed
		Integer DELIVER_FAIL = 2;
		// consumed
		Integer CONSUMED_SUCCESS = 3;
	}

}
