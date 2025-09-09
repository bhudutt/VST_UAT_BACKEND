package com.hitech.dms.app.service;

import com.hitech.dms.app.entity.LoginLog;

public interface LoginLogService {

    void insert(LoginLog loginLog);

    LoginLog selectByMsgId(String msgId);

}
