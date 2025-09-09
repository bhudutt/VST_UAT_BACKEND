/**
 * 
 */
package com.hitech.dms.web.service.common.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.service.common.CommonService;

/**
 * @author dinesh.jakhar
 *
 */
@Service
public class CommonServiceImpl implements CommonService {
	
	@Override
	public ResponseEntity<?> returnResponseWithStatusCode(int statusCode, Object t) {
		if(statusCode == 200) {
			return new ResponseEntity<>(
					t, HttpStatus.OK);
		}else if(statusCode == 201) {
			return new ResponseEntity<>(
					t, HttpStatus.CREATED);
		}else if(statusCode == 417) {
			return new ResponseEntity<>(
					t, HttpStatus.EXPECTATION_FAILED);
		}else if(statusCode == 304) {
			return new ResponseEntity<>(
					t, HttpStatus.NOT_MODIFIED);
		}else if(statusCode == 500) {
			return new ResponseEntity<>(
					t, HttpStatus.INTERNAL_SERVER_ERROR);
		}else if(statusCode == 204) {
			return new ResponseEntity<>(
					t, HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<>(
					t, HttpStatus.UNAUTHORIZED);
		}
	}
}
