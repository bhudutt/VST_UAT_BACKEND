/**
 * 
 */
package com.hitech.dms.app.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.app.constants.WebConstants;
import com.hitech.dms.app.entity.MsgLog;
import com.hitech.dms.app.repo.MsgLogDao;
import com.hitech.dms.app.repo.SmsMailDao;
import com.hitech.dms.app.util.JodaTimeUtil;

/**
 * @author dinesh.jakhar
 *
 */
@Service
public class MsgLogServiceImpl implements MsgLogService {
	
	@Autowired
	private MsgLogDao msgLogDao;
	@Autowired
	private SmsMailDao smsMailDao;
	
	@Override
	public void updateStatus(String msgId, Integer status) {
		// TODO Auto-generated method stub
//		MsgLog msgLog = new MsgLog();
//		msgLog.setMsgId(msgId);
//		msgLog.setStatus(status);
//		msgLog.setUpdateTime(new Date());
		boolean isUpdated = msgLogDao.updateStatus(msgId, status);
		if(isUpdated && status.compareTo(3) == 0) {
			smsMailDao.updateStatusBasedOnMsgId(msgId, WebConstants.SENT);
		}
	}

	@Override
	public MsgLog selectByMsgId(String msgId) {
		// TODO Auto-generated method stub
		return msgLogDao.selectByMsgId(msgId);
	}

	@Override
	public List<MsgLog> selectTimeoutMsg() {
		// TODO Auto-generated method stub
		return msgLogDao.selectTimeoutMsg();
	}

	@Override
	public void updateTryCount(String msgId, Date tryTime) {
		Date nextTryTime = JodaTimeUtil.plusMinutes(tryTime, 1);

//		MsgLog msgLog = new MsgLog();
//		msgLog.setMsgId(msgId);
//		msgLog.setNextTryTime(nextTryTime);
		msgLogDao.updateTryCount(msgId, nextTryTime);
	}

}
