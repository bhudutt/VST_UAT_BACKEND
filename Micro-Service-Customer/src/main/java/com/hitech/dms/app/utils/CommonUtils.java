/**
 * 
 */
package com.hitech.dms.app.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitech.dms.app.api.response.MessageCodeResponse;
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
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static void main(String[] args) {
		String m = "april";
		System.out.println("2022-2023".length());
		System.out.println(CommonUtils.isNumeric("april"));
		System.out.println(CommonUtils.isNumeric("2022-2023"));
		System.out.println(CommonUtils.isNumeric("april2022-2023"));
		Pattern pattern = Pattern.compile("[^0-9]*([0-9]+).*");
		Matcher numberMatcher = pattern.matcher(m.toString());
		System.out.println(numberMatcher.matches());
		
		 m = "2022-2023";
		numberMatcher = pattern.matcher(m.toString());
		System.out.println(numberMatcher.matches());
		
		 m = "april2022-2023";
		numberMatcher = pattern.matcher(m.toString());
		System.out.println(numberMatcher.matches());
		
		if(numberMatcher.matches() && "2022-2023".length() > 9) {
			System.out.println("true ==>");
		}else {
			System.out.println("false ==>");
		}
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
	public static MessageCodeResponse getCodeResponse(String msg){
		
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		codeResponse.setCode("EC400");
		codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
		codeResponse.setMessage(msg);
		return codeResponse;
	}
}
