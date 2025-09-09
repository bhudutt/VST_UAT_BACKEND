/**
 * 
 */
package com.hitech.dms.app.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.app.exceptions.ErrorDetails;

/**
 * @author dinesh.jakhar
 *
 */
@Component
@Lazy
public class CommonUtils {
	private Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	
	
	public StringBuilder objToJson(PublishModel publishModel) {
		ObjectMapper mapper = new ObjectMapper();
		StringBuilder json = new StringBuilder();
		try {
		  json.append(mapper.writeValueAsString(publishModel));
		  System.out.println("ResultingJSONstring = " + json);
		  //System.out.println(json);
		} catch (JsonProcessingException e) {
			logger.error(this.getClass().getName(), e);
		} catch(Exception e) {
			logger.error(this.getClass().getName(), e);
		}
		return json;
	}
	
	public static ErrorDetails getErrorDetails(BindingResult bindingResult){
		ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
		errorDetails.setCount(bindingResult.getErrorCount());
		errorDetails.setStatus(HttpStatus.BAD_REQUEST);
		List<String> errors = new ArrayList<>();
		bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
		errorDetails.setErrors(errors);
		return errorDetails;
	}
}
