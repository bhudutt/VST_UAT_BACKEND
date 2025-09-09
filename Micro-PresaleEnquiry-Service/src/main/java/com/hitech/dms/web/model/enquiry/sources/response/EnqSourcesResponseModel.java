/**
 * 
 */
package com.hitech.dms.web.model.enquiry.sources.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class EnqSourcesResponseModel {
	private Integer sourceId;
	private String sourceCode;
	private String sourceName;
	private String isFieldSource;
	private String isDigitalSource;
}
