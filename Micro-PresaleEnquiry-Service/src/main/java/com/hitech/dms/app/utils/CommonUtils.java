/**
 * 
 */
package com.hitech.dms.app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitech.dms.app.config.publisher.PublishModel;

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
}
