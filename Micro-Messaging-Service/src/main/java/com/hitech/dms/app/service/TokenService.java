package com.hitech.dms.app.service;

import javax.servlet.http.HttpServletRequest;

import com.hitech.dms.app.common.ServerResponse;

public interface TokenService {

    ServerResponse createToken();

    void checkToken(HttpServletRequest request);

}
