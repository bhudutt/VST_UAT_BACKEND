/**
 * 
 */
package com.hitech.dms.app.config.subscribers.extract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitech.dms.app.config.subscribers.models.PublishModel;

/**
 * @author dinesh.jakhar
 *
 */
public class StringToObjectModel {
	private Logger logger = LoggerFactory.getLogger(StringToObjectModel.class);
	
	public PublishModel objToJson(String json) {
		ObjectMapper mapper = new ObjectMapper();
		PublishModel publishModel = null;
		try {
			if(json != null && !json.equals("")) {
				publishModel = mapper.readValue(json, PublishModel.class);
				System.out.println("ResultingJSONstring To Obj = " + publishModel.toString());
			}
		} catch (JsonProcessingException e) {
			logger.error(this.getClass().getName(), e);
		}
		return publishModel;
	}
}
