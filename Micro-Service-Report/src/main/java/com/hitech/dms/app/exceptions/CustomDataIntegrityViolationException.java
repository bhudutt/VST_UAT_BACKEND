package com.hitech.dms.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class CustomDataIntegrityViolationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CustomDataIntegrityViolationException(String string) {
		super(string);
	}
}