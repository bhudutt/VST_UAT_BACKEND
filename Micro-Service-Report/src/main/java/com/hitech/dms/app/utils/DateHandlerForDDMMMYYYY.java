/**
 * 
 */
package com.hitech.dms.app.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author dinesh.jakhar
 *
 */
public class DateHandlerForDDMMMYYYY extends StdDeserializer<Date> {

	public DateHandlerForDDMMMYYYY() {
		this(null);
	}

	public DateHandlerForDDMMMYYYY(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
		String date = jsonparser.getText();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YYYY");
			return sdf.parse(date);
		} catch (Exception e) {
			return null;
		}
	}
}
