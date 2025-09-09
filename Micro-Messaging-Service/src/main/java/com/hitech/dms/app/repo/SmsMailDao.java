package com.hitech.dms.app.repo;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.app.model.mail.SmsMailRequestModel;

public interface SmsMailDao {
	public SmsMailRequestModel fetchEventMailByMailItemId(BigInteger mailItemId);
	public List<SmsMailRequestModel> fetchEventMailsForTrigger(String eventName, String status);
	public void updateStatus(BigInteger mailitemId, String status);
	public void updateStatusForQueue(BigInteger mailitemId, String msgId, String status);
	public void updateStatusBasedOnMsgId(String msgId, String status);
	public void updateStatusBasedOnMsgLLogStatus(BigInteger mailitemId, String status, int msgLogStatus);
}
