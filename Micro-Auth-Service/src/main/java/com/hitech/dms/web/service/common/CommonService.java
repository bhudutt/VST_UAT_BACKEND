package com.hitech.dms.web.service.common;

import org.springframework.http.ResponseEntity;

public interface CommonService {
	public ResponseEntity<?> returnResponseWithStatusCode(int statusCode, Object t);
}
