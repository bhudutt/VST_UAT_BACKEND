package com.hitech.dms.app.repo;

import java.util.Date;
import java.util.List;

import com.hitech.dms.app.entity.MsgLog;

public interface MsgLogDao {
	boolean updateStatus(String msgId, Integer status);

    MsgLog selectByMsgId(String msgId);

    List<MsgLog> selectTimeoutMsg();

    void updateTryCount(String msgId, Date tryTime);
}
