package com.hitech.dms.app.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.model.ApiError;
import com.hitech.dms.app.validatior.ValidationError;
import com.hitech.dms.app.validatior.ValidationErrorBuilder;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	private String INCORRECT_REQUEST = "INCORRECT_REQUEST";
	private String BAD_REQUEST_400 = "BAD_REQUEST";
	private String CONFLICT = "CONFLICT";

	@Override
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
//        List<String> errorList = ex
//                .getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(fieldError -> fieldError.getDefaultMessage())
//                .collect(Collectors.toList());
//        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorList);
//		return handleExceptionInternal(ex, errorDetails, headers, errorDetails.getStatus(), request);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Validation failed");

		ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
		errorDetails.setCount(ex.getBindingResult().getErrorCount());
		errorDetails.setStatus(HttpStatus.BAD_REQUEST);
		errorDetails.setMsg("Validation failed");
		List<String> errors = new ArrayList<>();
		BindingResult bindingResult = ex.getBindingResult();
		bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
		errorDetails.setErrors(errors);

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(errorDetails);
		return new ResponseEntity<>(userAuthResponse, HttpStatus.BAD_REQUEST);
	}

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@ExceptionHandler(RecordNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleUserNotFoundException(RecordNotFoundException ex,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(INCORRECT_REQUEST, details);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MissingHeaderInfoException.class)
	public final ResponseEntity<ErrorResponse> handleInvalidTraceIdException(MissingHeaderInfoException ex,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(BAD_REQUEST_400, details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

//	@ExceptionHandler(ConstraintViolationException.class)
//	public final ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
//			WebRequest request) {
//		List<String> details = ex.getConstraintViolations().parallelStream().map(e -> e.getMessage())
//				.collect(Collectors.toList());
//		ErrorResponse error = new ErrorResponse(BAD_REQUEST_400, details);
//		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//	}

	@ExceptionHandler(CustomDataIntegrityViolationException.class)
	public final ResponseEntity<ErrorResponse> dataIntegrityViolationException(CustomDataIntegrityViolationException ex,
			WebRequest request) {
		String[] detail = ex.getLocalizedMessage().split("Detail: Key ");
		ErrorResponse error = new ErrorResponse(CONFLICT, Arrays.asList(detail[1]));
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a 'required'
	 * request parameter is missing.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Missing Servlet Request Parameter Exception");

		String error = ex.getParameterName() + " parameter is missing";

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(new ApiError(BAD_REQUEST, error, ex));
		return buildResponseEntity(userAuthResponse, BAD_REQUEST);
	}

	/**
	 * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is
	 * invalid as well.
	 *
	 * @param ex      HttpMediaTypeNotSupportedException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Http Media Type Not Supported Exception");

		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(
				new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
		return buildResponseEntity(userAuthResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown when @Validated
	 * fails.
	 *
	 * @param ex the ConstraintViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Constraint Violation Exception.");

		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMsg("Validation error");
		apiError.addValidationErrors(ex.getConstraintViolations());

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(apiError);
		return buildResponseEntity(userAuthResponse, BAD_REQUEST);
	}

	/**
	 * Handles EntityNotFoundException. Created to encapsulate errors with more
	 * detail than javax.persistence.EntityNotFoundException.
	 *
	 * @param ex the EntityNotFoundException
	 * @return the ApiError object
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Entity Not Found Exception.");
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
		apiError.setMsg(ex.getMessage());

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(apiError);
		return buildResponseEntity(userAuthResponse, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is
	 * malformed.
	 *
	 * @param ex      HttpMessageNotReadableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
//        logger.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Http Message Not Readable Exception");

		String error = "Malformed JSON request";

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(new ApiError(HttpStatus.BAD_REQUEST, error, ex));

		return buildResponseEntity(userAuthResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 *
	 * @param ex      HttpMessageNotWritableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Http Message Not Writable Exception.");

		String error = "Error writing JSON output";

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
		return buildResponseEntity(userAuthResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle NoHandlerFoundException.
	 *
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("No Handler Found Exception.");

		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMsg(
				String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		apiError.setDebugMessage(ex.getMessage());

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(apiError);
		return buildResponseEntity(userAuthResponse, BAD_REQUEST);
	}

	/**
	 * Handle javax.persistence.EntityNotFoundException
	 */
	@ExceptionHandler(javax.persistence.EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Entity Not Found Exception.");

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(new ApiError(HttpStatus.NOT_FOUND, ex));

		return buildResponseEntity(userAuthResponse, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handle DataIntegrityViolationException, inspects the cause for different DB
	 * causes.
	 *
	 * @param ex the DataIntegrityViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			WebRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Data Integrity Violation Exception.");

		userAuthResponse.setResponseCode(codeResponse);

		if (ex.getCause() instanceof ConstraintViolationException) {
			userAuthResponse.setResponseData(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
			return buildResponseEntity(userAuthResponse, HttpStatus.CONFLICT);
		}
		userAuthResponse.setResponseData(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
		return buildResponseEntity(userAuthResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param ex the Exception
	 * @return the ApiError object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Method Argument Type Mismatch Exception");

		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMsg(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		apiError.setDebugMessage(ex.getMessage());

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(apiError);
		return buildResponseEntity(userAuthResponse, apiError.getStatus());
	}

	@ExceptionHandler({ IllegalArgumentException.class, NumberFormatException.class })
	public ResponseEntity<Object> IAExceptionHandler(Exception ex) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
		codeResponse.setMessage("Illegal Argument Or Number Format Exception.");

		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMsg("Illegal Argument Or Number Format Exception.");
		apiError.setDebugMessage(ex.getMessage());

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(apiError);
		return buildResponseEntity(userAuthResponse, BAD_REQUEST);
	}

	private ResponseEntity<Object> buildResponseEntity(HeaderResponse userAuthResponse, HttpStatus status) {
		return new ResponseEntity<>(userAuthResponse, status);
	}

//	@ExceptionHandler
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
//	public ValidationError handleException(MethodArgumentNotValidException exception) {
//		return createValidationError(exception);
//	}
//
//	private ValidationError createValidationError(MethodArgumentNotValidException e) {
//		return ValidationErrorBuilder.fromBindingErrors(e.getBindingResult());
//	}
}