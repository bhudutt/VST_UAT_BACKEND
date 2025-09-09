package com.hitech.dms.app.service;

import com.hitech.dms.app.common.ServerResponse;
import com.hitech.dms.app.dto.UserDto;

public interface LoginService {

    ServerResponse doLogin(UserDto userDto);

}
