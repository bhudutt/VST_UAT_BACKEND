package com.hitech.dms.app.service;

import java.util.Date;
import java.util.List;

import com.hitech.dms.app.entity.MsgLog;

public interface MsgLogService {

    void updateStatus(String msgId, Integer status);

    MsgLog selectByMsgId(String msgId);

    List<MsgLog> selectTimeoutMsg();

    void updateTryCount(String msgId, Date tryTime);
}
