/**
 * 
 */
package com.hitech.dms.app.exceptions;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ErrorDetails {
	private HttpStatus status;
	private String msg;
	private Integer count;
	private List<String> errors;

	public ErrorDetails(HttpStatus status, String message) {
		super();
		this.status = status;
		this.msg = message;
	}

	public ErrorDetails(HttpStatus status, String message, List<String> errors) {
		super();
		this.status = status;
		this.msg = message;
		this.errors = errors;
	}

	public ErrorDetails(HttpStatus status, String message, String error) {
		super();
		this.status = status;
		this.msg = message;
		errors = Arrays.asList(error);
	}
}
