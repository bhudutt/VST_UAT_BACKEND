package com.hitech.dms.app.exceptions.functional;

import static java.lang.String.join;

import java.util.List;

public class NullValueException extends FunctionalException {

	private static final long serialVersionUID = 4896893580503414663L;

	public NullValueException(String field) {
		super(FunctionalErrorCode.NOT_NULL_FIELD, field);
	}

	public NullValueException(List<String> fields) {
		super(FunctionalErrorCode.NOT_NULL_FIELDS, join(", ", fields));
	}
}