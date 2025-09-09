package com.hitech.dms.app.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitech.dms.app.common.ResponseCode;
import com.hitech.dms.app.common.ServerResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class AppControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public ServerResponse serviceExceptionHandler(ServiceException se) {
        return ServerResponse.error(se.getMsg());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ServerResponse exceptionHandler(Exception e) {
        log.error("Exception: ", e);
        return ServerResponse.error(ResponseCode.SERVER_ERROR.getMsg());
    }

}
