/**
 * 
 */
package com.hitech.dms.app.exceptions.functional;

import static java.lang.String.format;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * This class represents a functional exception.
 */
@EqualsAndHashCode(callSuper = true)
public class FunctionalException extends RuntimeException {

	private static final long serialVersionUID = 4273322132185545866L;

	@Getter
	private final FunctionalErrorCode errorCode;

	FunctionalException(String messageTemplate, FunctionalErrorCode errorCode, Throwable cause, String... arguments) {
		super(format(messageTemplate, arguments), cause);
		this.errorCode = errorCode;
	}

	FunctionalException(FunctionalErrorCode errorCode, String... arguments) {
		this(errorCode.getMessageTemplate(), errorCode, null, arguments);
	}

}
